ilog('-----------just for test luajava for lua 5.3------------')
import 'import_test'
import_function("---> Call import method argment.")

function callJavaFunction()
	dlog('-----> callJavaFunction began')
	local data = {
		data1 = 'tab_1', 
		data2 = 'tab_2', 
		data3 = 'tab_3'
	}
	dlog('-----> callJavaFunction param ')
	dlog(data.data1)
	dlog('-----> callJavaFunction param ' .. data.data1)
	javaFunctions.doWithTableParam(data)

	dlog('-----> callJavaFunction end')
end

callJavaFunction()