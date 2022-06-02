# BcClub
step1：电脑端打开保密观首页 http://www.baomi.org.cn 注册账号
step2：随机打开任意保密视频，抓取saveCoursePackage.do方法，获取token
step3：启动项目,浏览器运行 localhost:10080/pc-club/secrecy/add-token?token={你刚刚获取到的token}&special=true
考试：每次打开是不同的答案，建议电脑抓取手机包，抓取 getExamContentData.do 方法，返回体中的typeList即为答案
