COMP_CLASSPATH = "lib/*:src/java"
RUN_CLASSPATH = "bin:lib/*"

all: Main_comp

run: Main

Test1_comp:
	javac -classpath $(COMP_CLASSPATH) -d bin/ src/java/test/Test1.java
Test2_comp:
	javac -classpath $(COMP_CLASSPATH) -d bin/ src/java/test/Test2.java
Main_comp:
	javac -classpath $(COMP_CLASSPATH) -d bin/ src/java/Main.java

Test1: Test1_comp
	java -classpath $(RUN_CLASSPATH) test.Test1
Test2: Test2_comp
	java -classpath $(RUN_CLASSPATH) test.Test2 "Dom__ne _niversit%"
Main: Main_comp
	java -classpath $(RUN_CLASSPATH) Main
