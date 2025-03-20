#!/usr/bin/python3

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains
from selenium.common.exceptions import WebDriverException
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as ec
from selenium.webdriver.common.by import By

from bs4 import BeautifulSoup
from lxml import etree
from tabulate import tabulate
import requests
import time
import sys

def jtexpress(trackingCode):
    trackingURL = "https://www.jtexpress.my/track.php?awbs=" + trackingCode

    requestRet = requests.get(trackingURL, verify=False)
    
    if(requestRet.status_code == 200):
        try:
            print("[*] Checking: " + trackingCode)
            soup = BeautifulSoup(requestRet.text,'html.parser')               
            trackingResult = soup.findAll("div", {"class": "tracking-result-box-right-inner"})                        

            trackingRsl = []
            for i in range(len(trackingResult)):                
                #extracking tracking contents from the page
                soup = BeautifulSoup(str(trackingResult[i]),'html.parser')
                trackingDate = soup.find("div", {"class" : "tracking-point-date-time tracking-date"})
                trackingTime = soup.findAll("div", {"class" : "tracking-point-date-time"})
                
                if(i==0):
                    trackingDetails = soup.find("div", {"class" : "tracking-point-details latest-scanning"})
                else:
                    trackingDetails = soup.find("div", {"class" : "tracking-point-details"})          

                #reformat the output
                trackingDateArr = str(trackingDate).split('\n') 
                trackingDate = trackingDateArr[1].replace('<div>','').replace('</div>','') + " "
                trackingDate += trackingDateArr[2].replace('<div>','').replace('</div>','')

                trackingTimeArr = str(trackingTime[1]).split('\n')
                trackingTime = trackingTimeArr[1].replace('</div>', '').strip()

                trackingDetailArr = str(trackingDetails).split('<br/>')
                trackingInfo = trackingDetailArr[0].split('\n')[1].strip()
                trackingCity = trackingDetailArr[1].replace('City :', '').strip()
                trackingStatus = trackingDetailArr[2].replace('Status : <span style="color:;">', '').replace('Status : <span style="color:green;">', '').replace('Status : <span style="color:coral;">', '').replace('</span> </div>', '').replace('</span>', '').strip()
                remark = ""

                #show remarks if status => On Hold
                if(trackingStatus == "On Hold"):
                    remark = trackingDetailArr[3].replace('Remark: ', '').replace('</div>', '').strip()

                #append each result into trackingRsl List
                trackingRsl.append([str(trackingDate), str(trackingTime), str(trackingInfo), str(trackingCity), str(trackingStatus), str(remark)])                                       
                
            
            #check there's result or not          
            if(len(trackingRsl) == 0):
                print("[!] No Result Found.", end="\n\n")
            else:                        
                #print tracking result in table
                print("[*] Check complete print result: ", end="\n\n")
                print("Tracking Code: " + trackingCode, end="\n\n")
                print(tabulate(trackingRsl, headers=['Date', 'Time', 'Info', 'City', 'Status', 'Remark']), end="\n\n") 

        except Exception as e:
            print("[!] Something Went Wrong Here.")
            print(e)
            sys.exit(0)
    else:
        print('[!] ERROR Occured!')
        print("[!] Status Code: " + str(requestRet.status_code))
        sys.exit(0)
