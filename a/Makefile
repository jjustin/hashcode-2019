compile:
	javac $(shell ls *.java)

compile/%: %.java
	javac $*.java

start/%: compile/%
	java $*

run/%: %.class
	java $*

test/%: compile/%
	for f in $$(ls -1 *.txt | sed -e 's/\..*$$//'); do java $* < $$f.txt > $$f.out; done 

clean:
	rm -rf *.out *.class