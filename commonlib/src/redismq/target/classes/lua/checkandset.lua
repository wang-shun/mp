-- set new nx
local current = redis.call('SET', KEYS[1], ARGV[1], "NX")
return current