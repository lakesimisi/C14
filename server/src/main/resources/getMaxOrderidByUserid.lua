--
-- Created by IntelliJ IDEA.
-- User: admin
-- Date: 2018/11/2
-- Time: 17:42
-- To change this template use File | Settings | File Templates.
--

local v=redis.call("get","maxid"..":"..KEYS[1])

return v;

