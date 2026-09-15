# 百世物流 测试接口调用

百世OA接口，用于测试外部接口调用。

### 介绍
测试百世物流OA接口、CICD发布

- 原接口：https://oa.800best.com/best/getConfig
- 新测试接口：
    - https://oa.800best.club/best/api/getConfig
    - https://oa.800best.club/best/api/health
    - https://oa.800best.club/best/api/help

- 快运接口（已废弃）：https://v5.800best.com/v5/getConfig
- EDI接口：https://sgp-edi.800best.com/v5/getConfig
- 开放平台：https://open.800best.com/

### 接口认证
所有接口均采用 OAuth 2.0 认证机制，使用 access_token 作为访问凭据。
```
Authorization: Bearer {access_token}
Content-Type: application/json
```


### 接口列表
| 接口路径                  | 描述                 | 请求方法 | 参数示例                    |
|---------------------------|-----------------------|------------|----------------------------|
| `/best/api/getExpress`      | 获取指定快递信息       | `GET`        | `?tracking_no=` |
| `/best/api/delExpress`      | 删除指定快递记录       | `POST`       | `{"tracking_no": ""}` |
| `/best/api/listExpress`     | 获取快递记录列表       | `GET`        | `?page=1&page_size=20` |

### 🩺 健康检查接口
| 接口路径                    | 描述                   | 请求方法 | 返回示例                  |
|-----------------------------|-------------------------|------------|----------------------------|
| `/best/api/health`           | 健康检查                | `GET`        | `{ "status": "success" }`        |


