all: Main

Test1_comp:
	javac -classpath "bin:bin/*" -d bin/ src/java/test/Test1.java
Test2_comp:
	javac -classpath "bin:bin/*" -d bin/ src/java/test/Test2.java
Main_comp:
	javac -classpath "bin:bin/*" -d bin/ src/java/Main.java

Test1: Test1_comp
	java -classpath "bin:bin/*" test.Test1
Test2: Test2_comp
	java -classpath "bin:bin/*" test.Test2 "Dom__ne _niversit%"
Main: Main_comp
	java -classpath "bin:bin/*" Main
