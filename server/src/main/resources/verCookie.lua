--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2018/10/21
-- Time: 15:47
-- To change this template use File | Settings | File Templates.
--

local v=redis.call("exists",KEYS[1]);
local s;
if v==1 then
    s=redis.call("get",KEYS[1]);
else
    s="gc";
end;
return s