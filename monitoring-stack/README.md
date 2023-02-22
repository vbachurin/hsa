# Monitoring stack

## Setup steps
1. Start docker on your computer
2. Make sure `docker-compose` and `ab` are installed
3. In terminal make sure the current directory is `monitoring-stack`
4. Execute `docker-compose up`
5. Open `http://localhost:3000` in the browser
6. Authenticate Grafana using `admin/admin` credentials, set any new password 
7. Click `Add your first data source`, select `InfluxDB`
8. Set `URL` to `http://influxdb:8086`
9. In `Custom HTTP Headers` add one with `Header` = `Authorization`, `Value` = `Token <replace with DOCKER_INFLUXDB_INIT_ADMIN_TOKEN>`. Find `DOCKER_INFLUXDB_INIT_ADMIN_TOKEN` in `docker-compose.yml`
10. Set `Database` to `telegraf`
11. Press `Save & test` and make sure you see the `datasource is working. <N> measurements found` message
12. Staying in Grafana UI, go to `Dashboards`, click `New` -> `New dashboard` -> `Add a new panel` 
13. Make sure the newly added `InfluxDB` is selected as `Data source`
14. In the first query, `select measurement` = `cpu`, `field(value)` = `field(usage_user)`
15. Press `+ Query`, there select `select measurement` = `mem`, `field(value)` = `field(used_percent)`
16. Press `Save` to save the dashboard
17. Go to Terminal, run `ab -n 20000 localhost/index.php`

```
ab -n 20000 localhost/index.php                                                                                                                                      
This is ApacheBench, Version 2.3 <$Revision: 1901567 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 2000 requests
Completed 4000 requests
Completed 6000 requests
Completed 8000 requests
Completed 10000 requests
Completed 12000 requests
Completed 14000 requests
Completed 16000 requests
Completed 18000 requests
Completed 20000 requests
Finished 20000 requests


Server Software:        nginx/1.19.3
Server Hostname:        localhost
Server Port:            80

Document Path:          /index.php
Document Length:        1056 bytes

Concurrency Level:      1
Time taken for tests:   342.790 seconds
Complete requests:      20000
Failed requests:        19990
   (Connect: 0, Receive: 0, Length: 19990, Exceptions: 0)
Total transferred:      24448890 bytes
HTML transferred:       21188890 bytes
Requests per second:    58.34 [#/sec] (mean)
Time per request:       17.139 [ms] (mean)
Time per request:       17.139 [ms] (mean, across all concurrent requests)
Transfer rate:          69.65 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0      11
Processing:    12   17   4.6     16     245
Waiting:       12   17   4.5     16     243
Total:         12   17   4.6     16     245

Percentage of the requests served within a certain time (ms)
  50%     16
  66%     17
  75%     17
  80%     18
  90%     20
  95%     23
  98%     28
  99%     38
 100%    245 (longest request)
```

![image](https://user-images.githubusercontent.com/5364130/220699055-8e513c0c-c09c-466b-bd9b-e0af4aa7c10d.png)
