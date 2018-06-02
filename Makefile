all:
	(cd po-uuilib-201708311009; make $(MFLAGS) all)
	(cd mmt-core; make $(MFLAGS) all)
	(cd mmt-app; make $(MFLAGS) all)

clean:
	(cd po-uuilib-201708311009; make $(MFLAGS) clean)
	(cd mmt-core; make $(MFLAGS) clean)
	(cd mmt-app; make $(MFLAGS) clean)

install:
	(cd po-uuilib-201708311009; make $(MFLAGS) install)
	(cd mmt-core; make $(MFLAGS) install)
	(cd mmt-app; make $(MFLAGS) install)
