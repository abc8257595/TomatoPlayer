drop database if exists film;

create database film;

use film;

create table captiontest
(
 type  int unsigned,
 filmname Varchar(20) not null,
 admoment varchar(20) not null,
 keywords Varchar(10) not null,
 adname   varchar(50) not null,
 VideoPath Varchar(255) not null,
 tv1 Varchar(255)    not null,
 tv2 Varchar(255)    not null,
 phone1 Varchar(255)    not null,
 phone2 Varchar(255)    not null,
 adpath Varchar(255)    not null 
 )ENGINE=MyISAM DEFAULT CHARSET=utf8;


/*广告*/
 INSERT INTO captiontest values(1,'变形金刚3','00:30','口香糖','绿箭口香糖',
'/video/video.webm',
'http://202.104.110.178:8080/picture/big1.jpg',
'http://202.104.110.178:8080/picture/small1.png',
'http://202.104.110.178:8080/picture/ad3phone1.jpg',
'http://202.104.110.178:8080/picture/ad3phone2.jpg',
'http://www.wrigley.com.cn/'
);

/*吐槽一*/
 INSERT INTO captiontest (type,filmname, admoment,adname,phone1)values(4,'变形金刚3',
 '02:41','吐槽--劈腿男Sam',
'http://202.104.110.178:8080/tu/tu1.html'
);


/*吐槽二*/
 INSERT INTO captiontest (type,filmname, admoment,adname,phone1)values(4,'变形金刚3',
 '03:33','吐槽--伊利终于去祸害外国人了',
'http://202.104.110.178:8080/tu/tu2.html'
);


/*吐槽三
INSERT INTO captiontest (type,filmname, admoment,adname,phone1)values(4,'变形金刚3',
'04:25','吐槽--无力吐槽，只能吐血了',
'http://movie.douban.com/review/5033733/'
);
*/


/*主角介绍*/
INSERT INTO captiontest (type,filmname, admoment,adname,tv1,phone1)values(3,'变形金刚3',
'04:25','机器人:大黄蜂',
'http://202.104.110.178:8080/picture/character2.jpg',
'http://baike.baidu.com/subview/447834/8029817.htm#3'
);


/*影评*/
INSERT INTO captiontest (type,filmname, admoment,adname,phone1)values(5,'变形金刚3',
'04:42','人类已经无法阻止中国企业称霸世界了！！！',
'http://blog.renren.com/share/337600018/7784081475'
);




/*主角介绍*/
INSERT INTO captiontest (type,filmname, admoment,adname,tv1,phone1)values(3,'变形金刚3',
'01:57','女主角:罗茜·汉丁顿·惠特莉',
'http://202.104.110.178:8080/picture/character1.jpg',
'http://baike.baidu.com/view/5654968.htm?from_id=3950193&type=syn&fromtitle=罗茜·汉丁顿·惠特莉&fr=aladdin'
);

/*电影简介*/
INSERT INTO captiontest (type,filmname, admoment,adname,phone1)values(2,'变形金刚3',
'00:05','电影简介',
'http://202.104.110.178:8080/introduce.html'
);


/*剧情一*/
INSERT INTO captiontest (type,filmname, admoment,adname,phone1)values(2,'变形金刚3',
'01:45','剧情一',
'http://202.104.110.178:8080/ju/ju1.html'
);

/*剧情二*/
INSERT INTO captiontest (type,filmname, admoment,adname,phone1)values(2,'变形金刚3',
'04:18','剧情二',
'http://202.104.110.178:8080/ju/ju2.html'
);


/*测试关键字搜索功能*/
INSERT INTO captiontest (type,filmname, admoment,keywords,tv1)values(1,'机器人总动员',
'04:18','口香糖',
'http://202.104.110.178:8080/picture/walle.jpg'
);

INSERT INTO captiontest (type,filmname, admoment,keywords,tv1)values(1,'后会无期',
'04:18','口香糖',
'http://202.104.110.178:8080/picture/hou.png'
);


