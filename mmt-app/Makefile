JAVADIR=FIXME
JARNAME=mmt-app

all:
	(cd src; javac -cp $(JAVADIR)/po-uuilib.jar:../../mmt-core/mmt-core.jar -encoding UTF-8 `find . -name \*.java`)
	(cd src; jar cf ../$(JARNAME).jar `find . -name \*.class -o -name \*.java` )

clean:
	$(RM) $(JARNAME).jar `find . -name \*.class`
