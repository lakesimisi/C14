-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.17-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 mpet 的数据库结构
CREATE DATABASE IF NOT EXISTS `mpet` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mpet`;


-- 导出  表 mpet.account 结构
CREATE TABLE IF NOT EXISTS `account` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(80) NOT NULL,
  `password` varchar(60) DEFAULT NULL,
  `email` varchar(80) NOT NULL,
  `xm` varchar(80) NOT NULL,
  `address` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 7168 kB; InnoDB free: 6144 kB';

-- 正在导出表  mpet.account 的数据：~13 rows (大约)
DELETE FROM `account`;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` (`userid`, `username`, `password`, `email`, `xm`, `address`) VALUES
	(1, '1112123154', '1111', '123@123.com', '123', '12312'),
	(2, '121212', '111', '123@123.com', '111', '222'),
	(3, '12121244', '111', '123@123.com', '123', '123123'),
	(4, '121235454', '111', '123@124.com', '343434', '123'),
	(5, '12211', '111', 'wk@123.com', '121212', '121212'),
	(6, '1221233434', '111', 'wk@123.com', '121212', '1123123'),
	(7, '12222', '111', '123@123.com', '12312', '123123'),
	(8, '123', '123', '123@123', '123', '123'),
	(9, '12312', '111', '123@123.com', '123', '1232'),
	(10, '1231231344', '111', 'wk@123.com', '123', '222'),
	(11, '1231231`1', '111', '123@123.com', '12312', '123123'),
	(12, 'dsf', 'df', 'df', 'df', 'df'),
	(13, 'con', '999', '999', '999', '999'),
	(14, 'kkk', 'kkk', 'kkk', 'kk', 'kk');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;


-- 导出  过程 mpet.addCart 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart`(IN `in_itemid` vaRCHAR(50), IN `in_qty` INT, OUT `out_total` FLOAT, OUT `out_oid` INT)
BEGIN
	
	set @maxid=0;
	set @orderdate='';
	set @count=0;
	set @qty=0;
	
	select max(orderid),orderdate into @maxid,@orderdate from orders; 
	
	if  @orderdate is null then -- 代表订单还可以添加新商品
		
		 -- 查询本次添加的商品是否在同一订单下还有同种商品，
		-- 如果有，将进行修改数量，如果没有，是真正的新商品，只需要insert。
		
		select count(*) into @count
		
		from cart
		where orderid=@maxid and 
				itemid=in_itemid;
		if @count=0 then
		
			insert into cart(orderid,itemid,quantity)
			values( @maxid ,in_itemid,in_qty);
		
			
			
	   else
	   		select quantity into @qty
	   		from cart
	   		where orderid=@maxid and
	   		      itemid=in_itemid;
				
				call updateCart(@maxid,in_itemid,@qty+in_qty);	
		
			
		end if;
		
		
	else -- 日期为空的时候
		
			select max(orderid)+1 into @maxid from orders; 
			
			insert into orders(orderid)
			values( @maxid );
			
			insert into cart(orderid,itemid,quantity)
			values( @maxid ,in_itemid,in_qty);
		
	end if;
	commit;
	set out_oid:=@maxid;
	call queryCart(@maxid,out_total);
	
	
	
END//
DELIMITER ;


-- 导出  过程 mpet.addCart1 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart1`(IN `in_itemid` CHAR(50), IN `in_quantity` INT)
    COMMENT '增加购物车商品'
BEGIN
	set @odate='';
	set @oid=0;
	set @itemid='';
	set @qty=0;
	
	select orderid,orderdate into @oid,@odate
	from orders o
	where o.orderid=(select max(orderid) from orders);
	
--	select @oid;-- 最大id
--	select @odate;-- 最大id中的日期
	
	if @odate is null then-- 就说明这个订单并没有形成订单号可以继续购买
	
		select itemid,quantity into @itemid,@qty
		from cart c
		where orderid=@oid and itemid=in_itemid;
			
		if @itemid=''  then -- 新商品，直接添加
			
			insert into cart (orderid,itemid,quantity) 
			values(@oid,in_itemid,in_quantity);
			commit;
			
		
		else -- 有老商品，数量累加
			select @qty;
			select in_quantity;
			select @oid ;
			select @in_itemid;
			update cart 
			set quantity=in_quantity+@qty
			where orderid=@oid and itemid=in_itemid;
			
			commit;
		end if;
		
		
		
	else -- 需要形成新的订单号
		
	   set @oid:=@oid+1;
	   
	   insert into orders (orderid) values(@oid);
	   
	   insert into cart (orderid,itemid,quantity) 
		values(@oid,in_itemid,in_quantity);
	   
	   commit;
	   
		
	end if;
END//
DELIMITER ;


-- 导出  过程 mpet.addCart10 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart10`(IN `in_username` varchar(10), IN `in_iid` varchar(20), IN `in_quantity` INT, OUT `o_oid` INT)
begin
	set @oid:=0;
	set @odate:="";
	set @quantity:=0;
	
	select  orderid , orderdate into @oid, @odate
	from orders 
	where username = in_username
	order by username ,orderid desc
	limit 1;
	#select  @odate;
	if @odate!='' then -- 代表已经结算了，订单编号需要自增
		
		set @oid:=@oid+1;-- 最新的订单编号		
		#select @oid;
		-- 新增加主表
	 	insert into orders (username,orderid) values(in_username,@oid);
	 	
		-- 对购物车表进行添加数据
		insert into cart (username,orderid,itemid,quantity) 
		values(in_username,@oid,in_iid,in_quantity);
		
		
	else -- 还是老订单，需要判断item在当前购物车下是否存在编号一样的记录
		
		select quantity into @quantity
		from cart
		where username=in_username and itemid=in_iid and orderid=@oid;
		if @quantity='' then -- 代表是纯新数据
			
				insert into cart (username,orderid,itemid,quantity) values(in_username,@oid,in_iid,in_quantity);
		else -- 已经有数据了，需要新老数据叠加
				set @quantity=@quantity+in_quantity;
				update cart set quantity=@quantity
				where	username=in_username and itemid=in_iid and orderid=@oid;

			
		end if;	
	
		
	end if;
	commit;
	
	set o_oid:=@oid;
end//
DELIMITER ;


-- 导出  过程 mpet.addCart3 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart3`(IN `in_itemid` varchar(50), IN `in_qty` INT)
BEGIN
	
	set @oid=0;
	set @odate='';
	set @count=0;-- 是否存在老商品
	set @qty=0;-- 老商品的数量

	-- 先看看该购物车的情况，是否已经提交生成了订单
	select orderid,orderdate into @oid ,@odate
	from orders
	where orderid=(select max(orderid) from orders limit 1 );
	
	if @odate is null then -- 还可以在当前订单下购物
		-- 再次判断是纯新的商品，还是已经在同一个订单下有老的商品了
		-- 有老的商品，需要修改同订单下，同商品下的数量
		-- 没有老的商品，直接插入该商品
		select count(*) into @count
		from cart
		where orderid=@oid and itemid=in_itemid;
		if @count=0 then-- 新商品，只增加
		
			insert into cart(orderid,itemid,quantity) 
			values(@oid ,in_itemid,in_qty);
		else -- 修改老商品，数量做修改
			-- 取出老商品的数量，和现有数量累加
			select quantity into @qty
			from cart
			where orderid=@oid and itemid=in_itemid;
			
			call updateCart3(in_itemid,in_qty+@qty);
		end if;
	else
		-- 订单日期不等于空，代表需要新形成订单
		insert into orders(orderid) values(@oid+1);
		-- 在进入购物车数据
		insert into cart(orderid,itemid,quantity) 
			values(@oid+1 ,in_itemid,in_qty);
			
	end if;
	call queryCart3();

END//
DELIMITER ;


-- 导出  过程 mpet.addCart4 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart4`(IN `in_itemid` varchar(100), IN `in_qty` int)
begin
/*根据最大订单查询出该订单是否已经被使用了，如果没被完全使用完，
那么它的order_date字段是空*/
 set @oid=0;/*订单编号*/
 
 set @odate='';/*订单日期*/
 set @itemid='';
 set @qty=0;/*数量*/
 select orderid,orderdate into @oid,@odate
 from orders
 where orderid=(select max(orderid) from orders limit 1);

 /*判读orderdate是否存在值*/
 
 if  @odate is null then/*已经开始购买，只不过还没买完*/
 	/*新物品是否在已买的中同一订单下存在*/
 	select itemid,quantity into @itemid,@qty
 	from cart
 	where orderid=@oid and itemid=in_itemid;
 	
 
 	
 	
 	if @itemid='' then
 		insert into cart(orderid,itemid,quantity) values(@oid,in_itemid,in_qty);
 	
 		
 	else/*数量改变*/
 		
 	
 		
 		call updateCart4(in_itemid,@oid,@qty+in_qty);
 	 
 	end if;
 	
 	
 
 else/*还没购买新东西那，重新生成订单号*/
 	
   insert into orders (orderid) values(@oid+1);
   
   insert into cart(orderid,itemid,quantity) values(@oid+1,in_itemid,in_qty);
   
	set @oid=@oid+1;
 
 end if;
 commit;
 call queryCart4(@oid);
	
end//
DELIMITER ;


-- 导出  过程 mpet.addCart5 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart5`(IN `in_itemid` varchar(10), IN `in_quantity` int

	
)
begin
	set @id:=0;#orderid
	set @date:='';#orderdate
	set @iid:='';#itemid
	set @qty:=0;#quantity
	#1先取订单编号
	select orderid ,orderdate into @id,@date
	from orders	
	order by orderid desc
	limit 1;
	#2判断其@date是否为空，如果为空代表还没提交还可以在当前订单下继续购买
	#如果不为空，代表已经提交了，需要生产新订单编号
	if @date!='' then
		set @id:=@id+1;
		insert into orders(orderid) values(@id);
		#select 'Hello';
		#新订单形成后，下一句就是添加新的购物车
		insert into cart(orderid,itemid,quantity) 
			values(@id,in_itemid,in_quantity);
	else 	
		#对购物车标修改
		#3如果itemid相等，代表当前商品是老商品，只需要改数量
		#否则是新商品，重新加记录
		select itemid,quantity into @iid,@qty
		from cart c
		where c.orderid=@id and
		      c.itemid=in_itemid;
		if @iid='' then
		
			insert into cart(orderid,itemid,quantity) 
			values(@id,in_itemid,in_quantity);
		
		else  
			call updateCart5(@iid,@id,@qty+in_quantity);
		
		end if;
	end if;
	select * from cart where orderid=@id;
end//
DELIMITER ;


-- 导出  过程 mpet.addCart6 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart6`(IN `in_itemid` varchar(20), IN `in_quantity` int, IN `in_username` varchar(20), OUT `out_oid` int

)
begin
  set @oid:=0;
  set @itemid:="";
  set @qu:=0;# 老数量
  
  -- 以下取出没结账的最大订单编号
  select orderid into @oid
  from orders o
  where o.orderdate is null and username=in_username
  order by o.orderid desc
  limit 1;
 
  
  if @oid='' then #没有新开订单
  	
  	select orderid into @oid
  	from orders
  	where username=in_username
  	order by orderid desc
  	limit 1;
  	
  	set @oid:=@oid+1;
  	insert into orders (orderid,username)
  	values(@oid ,in_username) ;
  
  end if;
  
  select itemid,quantity into @itemid,@qu
  from cart c
  where c.orderid=@oid and c.itemid=in_itemid and username=in_username;
  
  if @itemid='' then -- 证明是纯新商品，我们应该insert
  	
  	insert into cart(orderid,itemid,quantity,username) 
	  values(@oid,in_itemid,in_quantity,in_username);
  	
  else # 证明该购物车中有该商品，需要更新数量
    update cart c
    set quantity=@qu+in_quantity
    where c.orderid=@oid and c.itemid=in_itemid and username=in_username;
     
     
  end if;
  set out_oid=@oid;

end//
DELIMITER ;


-- 导出  过程 mpet.addCart7 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart7`(IN `in_username` varchar(10), IN `in_itemid` varchar(10), IN `in_quantity` int(2)
, OUT `out_oid` INT)
begin
	#取得当前用户下的订单编号和日期，如果日期为null，代表它还没有提交
	#可以继续购物。
	set @id:=0;
	set @odate:='';
	set @oqu:=0;#老数量
	select  orderid ,orderdate into @id,@odate
	from orders 
	where username=in_username 
	order by orderid desc 
	limit 1;
	
	if @odate is not null then#证明，他已经购物完毕，需要 增加新订单编号
		set @id=@id+1;
		insert into orders(orderid,username)
		values(@id,in_username);
		
	end if;
	
	
	
		#但是还需要判断是否是已经买了同类产品
		
		select quantity into @oqu
		from cart
		where username=in_username and 
			orderid=@id and 
			itemid=in_itemid;	
					
			if @oqu='' then #没有同类
				insert into cart(username,orderid,itemid,quantity)
				values(in_username,@id,in_itemid,in_quantity);
			else#有同类，修改
	
				set @nqu=in_quantity+@oqu;#新数量
				
				call updateCart7(in_username,in_itemid,@nqu,@id);
			
			end if;
	
	
	


	set out_oid:=@id;

end//
DELIMITER ;


-- 导出  过程 mpet.addCart8 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart8`(IN `in_username` varchar(10), IN `in_itemid` varchar(10), IN `in_quantity` int	
, OUT `out_orderid` INT)
begin
	set @maxid:=0;
	set @odate:='';
	set @oquantity=0;
	set @iid='';
	
	select orderid ,orderdate into @maxid,@odate
	from orders
	where username=in_username 
	order by orderid desc
	limit 1;
	
	select @odate;
	if @odate is not  null  then# 如果条件为真，代表必须重新生成订单编号。
	
			select orderid into @maxid
			from orders		
			where username=in_username
			order by orderid desc
			limit 1;
		set @maxid=@maxid+1;
		
		insert into orders(orderid,username)
		values(@maxid,in_username);
	  
	end if;
	#还没买完商品吗，还可以继续购买
		#看购物车在当前订单编号下 当前用户下，当前买的产品下是否有相同产品
		#如果有相同产品，新老数量叠加。
		#如果是新商品，做新纪录存储
		select itemid,quantity into @iid, @oquantity
		from cart
		where username=in_username and
		 orderid=@maxid and
		 itemid=in_itemid;
		 
		 if @iid ='' then#没有老商品
		 	insert into cart(username,orderid,itemid,quantity)
		 	values(in_username,@maxid,in_itemid,in_quantity);
		 else#只能修改了
		 	set @nquantity=in_quantity+@oquantity;
		 
		 	call updateCart8(in_username,in_itemid,@nquantity,@maxid);
		 	
		 end if;
		

	set out_orderid:=@maxid;

end//
DELIMITER ;


-- 导出  过程 mpet.addCart9 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCart9`(
	in in_itemid varchar(20),
	in in_username varchar(20),
	in in_quantity int,
	out out_oid int
)
begin
	#1 取出订单日期为空，且是当前用户的那笔订单
	set @oid:=0;
	set @qua:=0;
	select orderid into @oid
	from orders o
	where o.orderdate is null and o.username=in_username
	limit 1;
	
	if @oid='' then #代表不能继续购物,必须重新生成订单
		
		select orderid into @oid
		from orders o
		where  o.username=in_username
		order by orderid desc
		limit 1;
		
		set @oid:=@oid+1; #计算出新的订单编号
		#新生成订单
		insert into orders (orderid,username) values(@oid,in_username);
		
	
		
	else # 代表还可以继续购物，还没进行结算，需要看是否在该订单下有老商品
		
		select quantity into @qua
		from cart c
		where c.orderid=@oid and c.username=in_username and c.itemid=in_itemid;
		if @qua='' then  #判断是否有老商品，纯新商品
			
				#再在该订单下生成新的购物数据
		
			insert into cart(orderid,username,itemid,quantity) 
				values(@oid,in_username,in_itemid,in_quantity);
		
		else # 有老商品
			
				#再在该订单下生成新的购物数据
			set @qua:=@qua+in_quantity;
			
			update  cart c set quantity=@qua 
			where c.orderid=@oid and c.username=in_username 
			and c.itemid=in_itemid;
		   
		end if;
	   	
	
		
	
	end if;
	set out_oid:=@oid;


end//
DELIMITER ;


-- 导出  表 mpet.cart 结构
CREATE TABLE IF NOT EXISTS `cart` (
  `userid` int(11) NOT NULL,
  `itemid` int(11) NOT NULL,
  `orderid` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`userid`,`itemid`,`orderid`),
  KEY `FK_cart_item` (`itemid`),
  KEY `FK_cart_orders` (`orderid`),
  CONSTRAINT `FK_account` FOREIGN KEY (`userid`) REFERENCES `account` (`userid`),
  CONSTRAINT `FK_cart_item` FOREIGN KEY (`itemid`) REFERENCES `item` (`itemid`),
  CONSTRAINT `FK_cart_orders` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  mpet.cart 的数据：~15 rows (大约)
DELETE FROM `cart`;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` (`userid`, `itemid`, `orderid`, `quantity`) VALUES
	(8, 1, 5, 1),
	(8, 4, 6, 3),
	(8, 5, 5, 2),
	(8, 7, 1, 3),
	(8, 7, 6, 1),
	(8, 8, 4, 3),
	(8, 10, 5, 2),
	(8, 11, 4, 1),
	(8, 11, 6, 1),
	(8, 17, 5, 2),
	(8, 20, 6, 2),
	(8, 22, 1, 8),
	(8, 23, 1, 1),
	(8, 27, 1, 5),
	(8, 27, 3, 1);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;


-- 导出  表 mpet.category 结构
CREATE TABLE IF NOT EXISTS `category` (
  `catid` varchar(10) NOT NULL,
  `name` varchar(80) DEFAULT NULL,
  `descn` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`catid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 7168 kB; InnoDB free: 6144 kB';

-- 正在导出表  mpet.category 的数据：~5 rows (大约)
DELETE FROM `category`;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`catid`, `name`, `descn`) VALUES
	('BIRDS', '鸟类', '<image src="${ppath}/static/images/birds_icon.gif"><font size="5" color="blue"> Birds</font>'),
	('CATS', '猫', '<image src="${ppath}/static/images/cats_icon.gif"><font size="5" color="blue"> Cats</font>'),
	('DOGS', '狗', '<image src="${ppath}/static/images/dogs_icon.gif"><font size="5" color="blue"> Dogs</font>'),
	('FISH', '鱼', '<image src="${ppath}/static/images/fish_icon.gif"><font size="5" color="blue"> Fish</font>'),
	('REPTILES', '爬虫类', '<image src="${ppath}/static/images/reptiles_icon.gif"><font size="5" color="blue"> Reptiles</font>');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;


-- 导出  过程 mpet.delCart 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `delCart`(IN `in_orderid` INT, IN `in_itemid` vARCHAR(50), OUT `out_total` FLOAT, OUT `out_oid` INT)
BEGIN
	
	delete from Cart
	where orderid=in_orderid and
	  		itemid=in_itemid;
	commit;
	set out_oid:=in_orderid;
	call queryCart(in_orderid,out_total);
END//
DELIMITER ;


-- 导出  过程 mpet.delCart1 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `delCart1`(IN `in_orderid` INT, IN `in_itemid` CHAR(50))
BEGIN

	delete
	from cart
	where orderid=in_orderid and
	 	   itemid=in_itemid ;
END//
DELIMITER ;


-- 导出  过程 mpet.delCart3 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `delCart3`(IN `in_orderid` INT, IN `in_itemid` varCHAR(50))
BEGIN
	delete
	from cart 
	where orderid=in_orderid and
	      itemid=in_itemid;
	call queryCart3();
END//
DELIMITER ;


-- 导出  过程 mpet.delCart4 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `delCart4`(
in in_itemid  varchar(10),
in in_oid  int
)
begin

delete from cart 
where itemid=in_itemid and
		orderid=in_oid;
commit;
call queryCart4(in_oid);


end//
DELIMITER ;


-- 导出  过程 mpet.delCart7 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `delCart7`(

	in in_username varchar(10),
	in in_itemid varchar(10),
	in in_oid int
)
begin

	delete from cart
	where username=in_username and itemid=in_itemid and orderid=in_oid;

end//
DELIMITER ;


-- 导出  过程 mpet.delCart8 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `delCart8`(
	in_username varchar(10),
	in_orderid int,
	in_itemid varchar(10)
)
begin
	
	delete from  cart
	where username=in_username 	and
	 orderid=in_orderid and 
	 itemid=in_itemid;

end//
DELIMITER ;


-- 导出  函数 mpet.func_get_split_string 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` FUNCTION `func_get_split_string`(`f_string` varchar(1000), `f_delimiter` varchar(5), `f_order` int) RETURNS varchar(255) CHARSET utf8
BEGIN
  -- Get the separated number of given string.
  declare result varchar(255) default '';
  set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1));
  return result;
END//
DELIMITER ;


-- 导出  函数 mpet.func_get_split_string_total 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` FUNCTION `func_get_split_string_total`(`f_string` varchar(1000), `f_delimiter` varchar(5)
) RETURNS int(11)
BEGIN
  -- Get the total number of given string.
  return 1+(length(f_string) - length(replace(f_string,f_delimiter,'')));
END//
DELIMITER ;


-- 导出  表 mpet.item 结构
CREATE TABLE IF NOT EXISTS `item` (
  `itemid` int(11) NOT NULL,
  `itemno` varchar(10) NOT NULL,
  `listprice` decimal(10,2) DEFAULT NULL,
  `productid` int(11) DEFAULT NULL,
  `unitcost` decimal(10,2) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `attr1` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`itemid`),
  KEY `FK_item_product` (`productid`),
  CONSTRAINT `FK_item_product` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 7168 kB; (`productid`) REFER `jpetstore/product';

-- 正在导出表  mpet.item 的数据：~28 rows (大约)
DELETE FROM `item`;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` (`itemid`, `itemno`, `listprice`, `productid`, `unitcost`, `status`, `attr1`) VALUES
	(1, 'EST_1', 16.50, 3, 10.00, 'P', 'Large'),
	(2, 'EST_10', 18.50, 11, 12.00, 'P', 'Spotted Adult Female'),
	(3, 'EST_11', 18.50, 16, 12.00, 'P', 'Venomless'),
	(4, 'EST_12', 18.50, 16, 12.00, 'P', 'Rattleless'),
	(5, 'EST_13', 18.50, 15, 12.00, 'P', 'Green Adult'),
	(6, 'EST_14', 58.50, 8, 12.00, 'P', 'Tailless'),
	(7, 'EST_15', 23.50, 8, 12.00, 'P', 'With tail'),
	(8, 'EST_16', 93.50, 7, 12.00, 'P', 'Adult Female'),
	(9, 'EST_17', 93.50, 7, 12.00, 'P', 'Adult Male'),
	(10, 'EST_18', 193.50, 1, 92.00, 'P', 'Adult Male'),
	(11, 'EST_19', 15.50, 2, 2.00, 'P', 'Adult Male'),
	(12, 'EST_2', 16.50, 5, 10.00, 'P', 'Small'),
	(13, 'EST_20', 5.50, 4, 2.00, 'P', 'Adult Male'),
	(14, 'EST_21', 5.29, 4, 1.00, 'P', 'Adult Female'),
	(15, 'EST_22', 135.50, 14, 100.00, 'P', 'Adult Male'),
	(16, 'EST_23', 145.49, 14, 100.00, 'P', 'Adult Female'),
	(17, 'EST_24', 255.50, 14, 92.00, 'P', 'Adult Male'),
	(18, 'EST_25', 325.29, 14, 90.00, 'P', 'Adult Female'),
	(19, 'EST_26', 125.50, 10, 92.00, 'P', 'Adult Male'),
	(20, 'EST_27', 155.29, 10, 90.00, 'P', 'Adult Female'),
	(21, 'EST_28', 155.29, 13, 90.00, 'P', 'Adult Female'),
	(22, 'EST_3', 18.50, 6, 12.00, 'P', 'Toothless'),
	(23, 'EST_4', 18.50, 5, 12.00, 'P', 'Spotted'),
	(24, 'EST_5', 18.50, 5, 12.00, 'P', 'Spotless'),
	(25, 'EST_6', 18.50, 9, 12.00, 'P', 'Male Adult'),
	(26, 'EST_7', 18.50, 9, 12.00, 'P', 'Female Puppy'),
	(27, 'EST_8', 18.50, 12, 12.00, 'P', 'Male Puppy'),
	(28, 'EST_9', 18.50, 11, 12.00, 'P', 'Spotless Male Puppy');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;


-- 导出  表 mpet.orders 结构
CREATE TABLE IF NOT EXISTS `orders` (
  `userid` int(11) NOT NULL,
  `orderid` int(11) NOT NULL,
  `orderdate` varchar(40) DEFAULT NULL,
  `totalprice` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`userid`,`orderid`),
  KEY `FK_fk_account_1` (`userid`),
  KEY `pk_orders_2` (`orderid`),
  CONSTRAINT `FK_fk_account_1` FOREIGN KEY (`userid`) REFERENCES `account` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  mpet.orders 的数据：~16 rows (大约)
DELETE FROM `orders`;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` (`userid`, `orderid`, `orderdate`, `totalprice`) VALUES
	(1, 1, NULL, NULL),
	(2, 1, NULL, NULL),
	(3, 1, NULL, NULL),
	(4, 1, NULL, NULL),
	(5, 1, NULL, NULL),
	(6, 1, NULL, NULL),
	(7, 1, NULL, NULL),
	(8, 1, NULL, NULL),
	(8, 2, NULL, NULL),
	(8, 3, 'Sat Nov 03 22:00:22 CST 2018', 18.50),
	(8, 4, 'Sat Nov 03 22:58:35 CST 2018', 296.00),
	(8, 5, 'Sat Nov 03 23:00:15 CST 2018', 951.50),
	(8, 6, 'Sat Nov 03 23:01:58 CST 2018', 405.08),
	(9, 1, NULL, NULL),
	(10, 1, NULL, NULL),
	(11, 1, NULL, NULL),
	(12, 1, NULL, NULL),
	(13, 1, NULL, NULL);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;


-- 导出  表 mpet.product 结构
CREATE TABLE IF NOT EXISTS `product` (
  `productid` int(11) NOT NULL AUTO_INCREMENT,
  `productno` varchar(10) NOT NULL,
  `catid` varchar(10) NOT NULL,
  `name` varchar(80) DEFAULT NULL,
  `descn` varchar(255) DEFAULT NULL,
  `pic` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`productid`),
  KEY `FK_fk_product_1` (`catid`),
  CONSTRAINT `FK_fk_product_1` FOREIGN KEY (`catid`) REFERENCES `category` (`catid`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 7168 kB; (`category`) REFER `jpetstore/category';

-- 正在导出表  mpet.product 的数据：~16 rows (大约)
DELETE FROM `product`;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`productid`, `productno`, `catid`, `name`, `descn`, `pic`) VALUES
	(1, 'AV-CB-01', 'BIRDS', 'Amazon Parrot', 'Great companion for up to 75 years', 'bird4.gif'),
	(2, 'AV-SB-02', 'BIRDS', 'Finch', 'Great stress reliever', 'bird1.gif'),
	(3, 'FI-FW-01', 'FISH', 'Koi', 'Fresh Water fish from Japan', 'fish3.gif'),
	(4, 'FI-FW-02', 'FISH', 'Goldfish', 'Fresh Water fish from China', 'fish2.gif'),
	(5, 'FI-SW-01', 'FISH', 'Angelfish', 'Salt Water fish from Australia', 'fish1.jpg'),
	(6, 'FI-SW-02', 'FISH', 'Tiger Shark', 'Salt Water fish from Australia', 'fish4.gif'),
	(7, 'FL-DLH-02', 'CATS', 'Persian', 'Friendly house cat, doubles as a princess', 'cat1.gif'),
	(8, 'FL-DSH-01', 'CATS', 'Manx', 'Great for reducing mouse populations', 'cat3.gif'),
	(9, 'K9-BD-01', 'DOGS', 'Bulldog', 'Friendly dog from England', 'dog2.gif'),
	(10, 'K9-CW-01', 'DOGS', 'Chihuahua', 'Great companion dog', 'dog4.gif'),
	(11, 'K9-DL-01', 'DOGS', 'Dalmation', 'Great dog for a Fire Station', 'dog5.gif'),
	(12, 'K9-PO-02', 'DOGS', 'Poodle', 'Cute dog from France', 'dog6.gif'),
	(13, 'K9-RT-01', 'DOGS', 'Golden Retriever', 'Great family dog', 'dog1.gif'),
	(14, 'K9-RT-02', 'DOGS', 'Labrador Retriever', 'Great hunting dog', 'dog5.gif'),
	(15, 'RP-LI-02', 'REPTILES', 'Iguana', 'Friendly green friend', 'lizard2.gif'),
	(16, 'RP-SN-01', 'REPTILES', 'Rattlesnake', 'Doubles as a watch dog', 'lizard3.gif');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;


-- 导出  表 mpet.profile 结构
CREATE TABLE IF NOT EXISTS `profile` (
  `userid` int(11) NOT NULL,
  `lang` varchar(80) NOT NULL,
  `catid` varchar(30) NOT NULL,
  PRIMARY KEY (`userid`),
  KEY `FK_Reference_7` (`catid`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`catid`) REFERENCES `category` (`catid`),
  CONSTRAINT `FK_Relationship_4` FOREIGN KEY (`userid`) REFERENCES `account` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  mpet.profile 的数据：~2 rows (大约)
DELETE FROM `profile`;
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;
INSERT INTO `profile` (`userid`, `lang`, `catid`) VALUES
	(12, 'eng', 'BIRDS'),
	(13, 'eng', 'BIRDS'),
	(14, 'eng', 'FISH');
/*!40000 ALTER TABLE `profile` ENABLE KEYS */;


-- 导出  过程 mpet.p_1 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_1`(IN `in_category` varchar(10), IN `in_pro` varchar(10), IN `in_item` varchar(10)
)
begin
   if in_category!="" then
   		select * from product p,category c 
			where p.catid=in_category and
   		p.catid=c.catid;
	elseif(in_pro!="") then
			select * from item i,product p 
			where i.productid=in_pro and
			i.productid=p.productid ;
	elseif(in_item!="") then
			select * from item i,product p
			where i.itemid=in_item and
			i.productid=p.productid ;		
   end if;
   


end//
DELIMITER ;


-- 导出  过程 mpet.p_3 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_3`(
	in in_itemid varchar(10),# 新买的宠物编号
	in in_qty int #新买的宠物的数量
	
)
begin
 
	#
 #1.找到进行中的订单编号
 set @orderid:='';
 set @quantity:=0;
 select orderid into @orderid
 from orders
 where orderdate is null;
 #如果没有进行中的订单，就需要把当前最大的订单编号取出来
 
 if  @orderid="" then
 	select max(orderid)+1 into @orderid
 	from orders;
 	insert into orders(orderid) values(@orderid);
 	commit;
 end if;
 
 #2.判断在当前订单中的购物车上是否有同一宠物
 select quantity into @quantity
 from cart
 where orderid=@orderid and itemid=in_itemid;
 #3把新老宠物的数量进行叠加
 if @quantity = 0 then#代表是新的宠物
 
 	insert into cart (orderid,itemid,quantity) values(@orderid,in_itemid,in_qty);
 elseif  @quantity > 0 then#之前有同itemid的老宠物
 	update cart set quantity=in_qty+@quantity
	where orderid=@orderid and
   itemid=in_itemid;
 
 end if;
 commit;
 

end//
DELIMITER ;


-- 导出  过程 mpet.p_4 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_4`(#针对当前订单下的购物车进行查询所有宠物


	
)
begin
 set @orderid:='';
 select orderid into @orderid
 from orders 
 where orderdate is null;
 
 select * from cart c ,item i,product p
 where c.itemid=i.itemid and
 		 i.productid=p.productid and
 		 c.orderid=@orderid;
 		 

end//
DELIMITER ;


-- 导出  过程 mpet.p_5 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_5`(
	in in_orderid int,
	in in_itemid varchar(10)
)
begin
	delete from cart
	where orderid=in_orderid and
	itemid=in_itemid;
	
	commit;

end//
DELIMITER ;


-- 导出  过程 mpet.p_6 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_6`(
	in in_orderid int,
	in in_itemid varchar(10),
	in in_quantity int
)
begin
	update cart 
	set quantity=in_quantity
	where orderid=in_orderid and
	      itemid=in_itemid;
	
	
	commit;

end//
DELIMITER ;


-- 导出  过程 mpet.p_7 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_7`(
  in in_catid varchar(10),
  in in_proid varchar(10),
  in in_itemid varchar(10)
	
	
)
begin

 if in_catid!="" then
 	select *
 	from product p
 	inner join category c on p.catid=c.catid
 	where p.catid=in_catid;
 elseif in_proid!="" then
 	
 	select *
 	from item i
 	inner join product p on i.productid=p.productid
 	inner join category c on p.catid=c.catid
 	where p.productid=in_proid;
 elseif in_itemid!="" then
 	
 	select *
 	from item i
 	inner join product p on i.productid=p.productid
 	inner join category c on p.catid=c.catid
 	where i.itemid=in_itemid;
 end if;

end//
DELIMITER ;


-- 导出  过程 mpet.p_8 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_8`(	
 in in_itemid varchar(10)	,# 商品编号
 in in_quantity int,#数量
 out out_orderid int #最新的orderid
  			

)
begin 
  #1先判断该购物车是否形成了订单，
  # 如果其orderdate字段为空，证明他还可以继续购物。
  #订单编号就是该编号。
  #如果，如果其orderdate字段不为空，证明订单已经形成，那么重新找个最大订单编号  
  
	set @maxid:=0;
	set @odate:="";
	
   select orderid,orderdate into @maxid, @odate from orders order by orderid desc limit 1;
   
   if @odate is null then	#这是需要修改老订单	
		set @maxid:=@maxid;
		
		 #增加购物车 cart
		
		
      set @oldqua:=0;
      set @iid:="";
      
      #判断是否是同一订单下的新商品
      
      select itemid into @iid
      from cart 
      where orderid=@maxid and itemid=in_itemid;
      
      if @iid='' then#同一订单下该商品不存在
      	
      	insert into cart (orderid,itemid,quantity) 
			values(@maxid,in_itemid,in_quantity);
      
		else#同一订单下该商品存在
		
		  	
		  	#如果同一订单编号在同个商品，那么数量需要累加
      
	      select quantity into @oldqua
	      from cart
	      where orderid=@maxid and itemid=in_itemid;
	      
	      # 累加之后再送入当前顶单编号下的该商品。
	      update cart set quantity=@oldqua+in_quantity
	      where orderid=@maxid and itemid=in_itemid;
		  	
      end if;
      
      
      
		
   else      #这是新订单
   
      set @maxid:=@maxid+1;
      
      insert into orders(orderid)
      value(@maxid);
      
      insert into cart (orderid,itemid,quantity) 
		values(@maxid,in_itemid,in_quantity);

   end if;
   
   set out_orderid:=@maxid;
      
end//
DELIMITER ;


-- 导出  过程 mpet.queryCart 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryCart`(IN `in_orderid` INT, OUT `out_total` fLOAT)
BEGIN
	
		
	   select sum(i.listprice*c.quantity) into out_total 
	   from item i,cart c
	   where  c.itemid=i.itemid and  c.orderid=in_orderid;
	  
	   
		select * 
		from 	product p,category c,item i,cart a
		where  
		      p.catid=c.catid and
		      i.productid=p.productid and
		      a.itemid=i.itemid and
		      a.orderid=in_orderid;
	   
	   
	
END//
DELIMITER ;


-- 导出  过程 mpet.queryCart1 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryCart1`()
BEGIN
	set @oid=0;
	select orderid into @oid
	from orders o
	where o.orderid=(select max(orderid) from orders);
	
	
	select *
	from cart c,item i,product p,orders o
	where 
	      c.orderid=@oid and
	      c.itemid=i.itemid and
	      i.productid=p.productid and
	      c.orderid=o.orderid;
END//
DELIMITER ;


-- 导出  过程 mpet.queryCart3 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryCart3`()
BEGIN
	set @oid=0;
   select max(orderid) into @oid from orders limit 1 ;
	
	select *
	from cart c ,item i ,product p,category ca
	where c.orderid=@oid and
	      c.itemid=i.itemid and
	      p.productid=i.productid and
	      p.catid=ca.catid;
END//
DELIMITER ;


-- 导出  过程 mpet.queryCart4 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryCart4`(IN `in_oid` int

)
begin
	
	select *
	from cart c,product p,item i,category c1
	where orderid=in_oid and
	      c.itemid=i.itemid and
	      i.productid=p.productid and
	      p.catid=c1.catid;
	
end//
DELIMITER ;


-- 导出  过程 mpet.queryCart6 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryCart6`(IN `in_username` varchar(20)


)
begin
  
  set @oid:='';
  
  select orderid into @oid
  from orders 
  where username=in_username and orderdate is null;
  
  
  select *
  from cart c
  where c.orderid=@oid 
 
  and username=in_username;

end//
DELIMITER ;


-- 导出  过程 mpet.queryCart7 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryCart7`(

	in in_username varchar(10),
	in in_oid int
)
begin

	select *
	from cart
	where username=in_username and
	orderid=in_oid;

end//
DELIMITER ;


-- 导出  过程 mpet.queryCart8 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryCart8`(
	in_username varchar(10),
	in_orderid int
	
)
begin
	
	select *  from  cart
	where username=in_username 	and
	 orderid=in_orderid;
	
end//
DELIMITER ;


-- 导出  过程 mpet.queryPet 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryPet`(IN `in_category` CHAR(10), IN `in_pro` CHAR(10), IN `in_item` CHAR(10))
BEGIN

	
	/*
		in_category:宠物种类，为了查询product表
		in_pro:宠物产品,为了查询item表
		in_item:宠物项目，为了查询单个项目 为了查询item表
		

	*/
	if in_category!='' then
	
		select * from 
			product p,category c
		where  
		      p.catid=in_category and
				p.catid=c.catid;      
		      
	elseif in_pro!='' then
		
		select * 
		from 	product p,category c,item i
		where  i.productid=in_pro and
		       p.catid=c.catid and
		       i.productid=p.productid;
		      
	
		      
	elseif in_item!='' then	      
			select * 
			from 	item i,product p
			where  i.itemid=in_item and
			       i.productid=p.productid;
			      
		
	end if;
	


END//
DELIMITER ;


-- 导出  过程 mpet.queryPet2 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryPet2`(IN `in_cate` CHAR(50), IN `in_pro` CHAR(50), IN `in_item` CHAR(50))
BEGIN

	if in_cate!=''  then /* 通过种类进行查询产品*/
		select p.*,c.*,p.name pname
		from category c, product p
		where c.catid=p.catid and p.catid=in_cate;
		
		
	
	elseif in_pro!='' then /* 通过产品进行查询项目*/
	
		select *
		from Item i,product p 
		where i.productid=p.productid and 
			   i.productid=in_pro;
		
	
   elseif in_item!='' then/* 通过项目进行查询更详细的项目内容*/
   	
   	select p.*,i.*,p.name pname
		from Item i,product p 
		where i.productid=p.productid and 
			   i.itemid=in_item;
	end if;
	
	
	


END//
DELIMITER ;


-- 导出  过程 mpet.queryPet3 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryPet3`(IN `in_cate` VARCHAR(50), IN `in_pro` VARCHAR(50), IN `in_item` VARCHAR(50))
BEGIN
	if in_cate!=''  then -- 根据种类查询产品
		select p.*,c.*
		from category c,product p
		where p.catid=in_cate and p.catid=c.catid;
		
	
	
	elseif in_pro!=''  then -- 根据产品查询商品
	
		select *
		from product p,item i
		where i.productid=in_pro and p.productid=i.productid;
	else
		select *
		from item i,product p
		where i.itemid=in_item and
		      i.productid=p.productid;
	
		
	end if;


END//
DELIMITER ;


-- 导出  过程 mpet.queryPet4 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `queryPet4`(
	in in_catid varchar(10),
	in in_productid varchar(10),
	in in_itemid varchar(10)
)
begin
	if in_catid!="" then # 通过种类去查产品
		select *
		from product p
		inner join category c on(p.catid=c.catid)
		where p.catid=in_catid;
	elseif in_productid!="" then		
		select *
		from item i
		inner join product p on(p.productid=i.productid)
		inner join category c on(p.catid=c.catid)
		where i.productid=in_productid;
	elseif in_itemid!="" then	
		select *
		from item i
		inner join product p on(p.productid=i.productid)
		inner join category c on(p.catid=c.catid)
		where i.itemid=in_itemid;
			
	end if;

end//
DELIMITER ;


-- 导出  过程 mpet.querypet5 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `querypet5`(
	in in_catid varchar(20),
	in in_proid varchar(20),
	in in_itemid varchar(20)
)
begin

	if in_catid!=""  then
		select *
		from  product p
		inner join category c on (c.catid=p.catid)
		where p.catid=in_catid;
	end if;
	if in_proid !="" then
	   
		select *
		from  item i
		inner join product p on (i.productid=p.productid)
		inner join category c on (c.catid=p.catid)		
		where i.productid=in_proid;

	end if;
	
	
	if in_itemid !="" then
	   
		select *
		from  item i
		inner join product p on (i.productid=p.productid)
		inner join category c on (c.catid=p.catid)		
		where i.itemid=in_itemid;

	end if;
end//
DELIMITER ;


-- 导出  过程 mpet.sp_print_result 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_print_result`(
IN f_string varchar(1000),IN f_delimiter varchar(5)
)
BEGIN
  -- Get the separated string.
  declare cnt int default 0;
  declare i int default 0;
  set cnt = func_get_split_string_total(f_string,f_delimiter);
  drop table if exists tmp_print;
  create temporary table tmp_print (num int not null);
  while i < cnt
  do
    set i = i + 1;
    insert into tmp_print(num) values (func_get_split_string(f_string,f_delimiter,i));
  end while;
  select * from tmp_print;
  
END//
DELIMITER ;


-- 导出  过程 mpet.updateCart 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateCart`(IN `in_oid` INT, IN `in_itemid` CHAR(50), IN `in_qty` INT)
BEGIN
	
	update cart c
   set quantity=in_qty
	where c.orderid=in_oid and 
	      c.itemid=in_itemid;
   commit;
	
	
	
	
END//
DELIMITER ;


-- 导出  过程 mpet.updateCart3 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateCart3`(IN `in_itemid` VARCHAR(50), IN `in_qty` INT)
BEGIN
	set @oid=0;
	select orderid into @oid 
	from orders
	where orderid=(select max(orderid) from orders limit 1 );
	update cart
	set quantity=in_qty
	where orderid=@oid  and
	      itemid=in_itemid;
END//
DELIMITER ;


-- 导出  过程 mpet.updateCart4 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateCart4`(IN `in_itemid` varchar(10), IN `in_oid` int, IN `in_qty` int
)
begin

update cart set quantity=in_qty  
where  orderid=in_oid and itemid=in_itemid;
commit;



end//
DELIMITER ;


-- 导出  过程 mpet.updateCart5 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateCart5`(
	in in_itemid varchar(10),	
	in in_oid int,
	in in_qty int
)
begin
		 			
		update cart
		set quantity=in_qty
		where orderid=in_oid and itemid=in_itemid;
			
	

	
end//
DELIMITER ;


-- 导出  过程 mpet.updateCart6 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateCart6`(IN `in_username` varchar(20), IN `in_orderid` varchar(20), IN `in_itemid` varchar(20), IN `in_qty` int


)
begin
    update cart c
    set quantity=in_qty
    where c.orderid=in_orderid and c.itemid=in_itemid
    and username=in_username;
     

end//
DELIMITER ;


-- 导出  过程 mpet.updateCart7 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateCart7`(IN `in_username` varchar(10), IN `in_itemid` varchar(10), IN `in_quantity` int(2), IN `in_orderid` int
)
begin

	
	

	update cart set quantity=in_quantity 
	where username=in_username and 
		itemid=in_itemid and 
		orderid=in_orderid;

	

	

end//
DELIMITER ;


-- 导出  过程 mpet.updateCart8 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateCart8`(IN `in_username` varchar(10), IN `in_itemid` varchar(10), IN `in_quantity` int	
, IN `in_orderid` INT)
begin

		 
		 	update cart set quantity=in_quantity
		 		where username=in_username and
				 orderid=in_orderid and
				 itemid=in_itemid;
		


end//
DELIMITER ;


-- 导出  过程 mpet.updateCartB 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateCartB`(IN `in_olist` varchar(1000), IN `in_qlist` varchar(1000), IN `in_dlist` varchar(1000), IN `f_delimiter` varchar(5)
, OUT `out_total` FLOAT, OUT `out_oid` INT)
BEGIN
	
  /*
	  in_olist-- 要分割的orderid字符串
	  in_qlist-- 要分割的quantity字符串
	  in_dlist-- 要分割的itemid字符串
	  f_delimiter-- 分隔符
	*/	

  set @cnt = func_get_split_string_total(in_olist,f_delimiter); 
  set @i=0;
  while @i < @cnt  do
    set @i = @i + 1;    
    set @orderid=0+func_get_split_string(in_olist,f_delimiter,@i);
    set @itemid=func_get_split_string(in_dlist,f_delimiter,@i);
    set @quantity=0+func_get_split_string(in_qlist,f_delimiter,@i);    
   
    
    update cart set quantity=@quantity	 
	 where orderid=@orderid
	 and itemid=trim(@itemid); 
     
  end while; 
   commit;
  set out_oid:=@orderid;
  -- select @cnt;
  
   call queryCart(@orderid,out_total);
END//
DELIMITER ;


-- 导出  过程 mpet.updateOrders 结构
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateOrders`(IN `in_orderid` INT, IN `in_total` FLOAT)
BEGIN
	update orders 
	set orderdate=now() ,
	totalprice=in_total

	where orderid=in_orderid;
	commit;
END//
DELIMITER ;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
