--
-- Created by IntelliJ IDEA.
-- User: admin
-- Date: 2018/10/30
-- Time: 10:00
-- To change this template use File | Settings | File Templates.
--

local v=redis.call("exists",KEYS[1]);
if v==0 then
    redis.call("set",KEYS[1],KEYS[2])
end;