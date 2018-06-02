Compile:

	-> export CLASSPATH=<folder>/mmt-core/mmt-core.jar:<folder>/mmt-app/mmt-app.jar:<folder>/po-uuilib-201708311009/po-uuilib.jar

	-> enter MakeFile inside mmt-app folder

	-> change the JAVADIR variabla value to <folder>/po-uuilib-201708311009

	(inside the project folder)

	-> make

Run:
	-> java -Dimport=file.txt -Din=file.in -Dout=file.myout mmt.app.App importfile.txt

	(Note that import, in and out files are optional)

Import file structure:

	(IMPORTANT NOTE: THE PROJECT DOES NOT CONTEMPLATE ERROR VERIFICATIONS IN THE IMPORT FILE)

	Passengers:
	
		-> PASSENGER|<passenger name>

	Services (represents a single train - starts at A and goes through B, C.... and reaches Z - in the project we assume it happends every day):

		-> SERVICE|<service id>|<service price>|<time leaving station 1>|<station 1 name>|...|<time leaving station n>|<station n name>
	 
	Itineraries (set of parts of services - basically a path using multiple train lines):

		-> ITINERARY|<passenger id>|<date>|<service id>/<departing station>/<arriving station>|...|<service id>/<departing station>/<arriving station>

Example import file:

	PASSENGER|Jimmy Page
	PASSENGER|Axl Rose
	SERVICE|123|11.2|08:00|Lisbon|08:45|Madrid|09:05|Paris
	SERVICE|22|16.6|08:35|Oslo|08:47|Madrid|09:05|Rome|09:27|Vatican
	SERVICE|1|31.9|11:00|Rome|11:20|Turin
	SERVICE|666|66.6|09:30|Vatican|12:35|Prague
	ITINERARY|0|1977-02-12|123/Lisbon/Madrid|22/Madrid/Rome|1/Rome/Turin 	(Passeger 0 - Jimmy Page - goes from Lisbon to Turin using services 123, 22, 1 on the 12th February 1977)
	ITINERARY|1|2018-01-01|22/Oslo/Vatican|666/Vatican/Prague 		(Passeger 1 - Axl Rose - goes from Oslo to Prague using services 1, 666 on the 1st January 2018)
