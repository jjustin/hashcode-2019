compile:
	javac $(shell ls *.java)

compile/%: %.java
	javac $*.java

start/%: compile/%
	java $*

run/%:
	java $*

test/%: compile/%

	for f in $$(ls -1 *.in | sed -e 's/\..*$$//'); do java $* < $$f.in > $$f.out; done 

clean:
	rm -rf *.out *.class