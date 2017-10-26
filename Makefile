COMP_CLASSPATH = "lib/*:src/java"
RUN_CLASSPATH = "bin:lib/*"

all: Main_comp

run: Main

Question10_comp:
	javac -classpath $(COMP_CLASSPATH) -d bin/ src/java/tests/Question10.java
Question11_comp:
	javac -classpath $(COMP_CLASSPATH) -d bin/ src/java/tests/Question11.java
Main_comp:
	javac -classpath $(COMP_CLASSPATH) -d bin/ src/java/Main.java

Question10: Question10_comp
	java -classpath $(RUN_CLASSPATH) tests.Question10
Question11: Question11_comp
	java -classpath $(RUN_CLASSPATH) tests.Question11 "Dom__ne _niversit%"
Main: Main_comp
	java -classpath $(RUN_CLASSPATH) Main
