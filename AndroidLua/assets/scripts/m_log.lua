--
-- Author: ZhangHaihai(drivedreams@163.com)
-- Date: 2014-11-11 16:37:58
--

function ilog(msg)
 
  LuaLog:i("[L-I]" .. getPosition() .. msg)
end

function wlog(msg)
  LuaLog:w("[L-W]" .. getPosition() .. msg)
end

function dlog(msg)
  LuaLog:d("[L-D]" .. getPosition() .. msg)
end

function vlog(msg)
  LuaLog:w("[L-V]" .. getPosition() .. msg)
end

function elog(msg)
  LuaLog:w("[L-E]" .. getPosition() .. msg)
end

function getPosition()
  local info = debug.getinfo(3) 
  local sourceName = " "
  
  if info.source and string.len(info.source) > 20 then
    sourceName = string.sub(info.source, 1, 20)
  elseif info.source  then
    sourceName = info.source
  end
  sourceName = string.gsub(sourceName, "\n+", " ")
  return "[" .. (sourceName  or " " ) .. ":" .. info.currentline .."]"
 
end

