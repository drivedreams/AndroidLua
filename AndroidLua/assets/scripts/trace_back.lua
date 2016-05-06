-- Trace back --
-- Author: ZhangHaihai(drivedreams@163.com)
-- Date: 2014-11-11 16:37:58
--


function printTraceBack(errstr)
 
  if errstr then
    elog(errstr)
  end
 
  local traceback = debug.traceback()
  if traceback then
    elog(traceback) 
  end
  
end

