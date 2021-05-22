# robotest
Api's are:
/getOutputFile -> API will take input xml file and will generate the output xml file in your project path as output.xml post computing the output basis of input 
/getOutput  -> API will take input xml file and will return xml response with the output values

Sample Input xml used:-
<?xml version="1.0" encoding="UTF-8"?>
<positions>
<position o ="0">
	<direction>N</direction>
	<x>10</x>
	<y>10</y>
</position>
<position o="1">
		
		<L>90</L>
		<R>0</R>
		<F>10</F>
		<B>0</B>
</position>
<position o="2">
		<L>0</L>
		<R>180</R>
		<F>0</F>
		<B>20</B>
</position>
</positions>

Output generated for above input:
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<position>
<direction>E</direction>
<x>-20</x>
<y>10</y>
</position>
