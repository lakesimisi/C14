--
-- Created by IntelliJ IDEA.
-- User: admin
-- Date: 2018/11/2
-- Time: 17:43
-- To change this template use File | Settings | File Templates.
--


redis.call("set","carts:"..KEYS[1]..":"..KEYS[2]..":"..KEYS[3],KEYS[4]);