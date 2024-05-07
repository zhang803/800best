// 百世生活OA调用接口
// code by Zhang.lock@800best.com
//

const app = {
  key: 'oakey800best@com'
  // domain: 'oa.800best.com'
  domain: 'oa.800best.life'
  loginUrl: '/best/oa'
  username: 'best_oa'
  password: 'your_password' // password: 'Abc2467#123'
  bid:'oapi_0x5f3759df_v1_cli_a36ae2528df0900b'
  apidev: 'http://oa.800best.life/best/oa'

};  

fetch(app.apidev, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Cache-Control': 'max-age=0',
    'Upgrade-Insecure-Requests': '1',
    'Origin': `http://${app.domain}`
  },
  body: new URLSearchParams({
    username: app.username,
    password: app.password
  })
})
.then(response => response.text())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
