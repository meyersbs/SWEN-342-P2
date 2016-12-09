#***********Variables************
JAR_TSA 	= target/tsa-airport-1.0-SNAPSHOT.jar
JAR_AKKA	= lib/akka-actor_2.11-2.4.12.jar
JAR_SCALA	= lib/scala-library-2.11.8.jar
JAR_CONFIG	= lib/config-1.3.0.jar
PROG		= edu.swen342.Driver
MVN 		= mvn package 
TSA_FILE	= tsa.txt

HELP_LIST	 	 = "Make options: tsa, file (will output results to an tsa.txt file), help, clean"
#****Compiler & Directives****
JAVA_FLAG 	= java -cp


#***********Targets***********

# - Should make 
# ... java -cp target/tsa-airport-1.0-SNAPSHOT.jar:lib/
#		akka-actor_2.11-2.4.12.jar:lib/
#		scala-library-2.11.8.jar:lib/
#		config-1.3.0.jar edu.swen342.Driver

.PHONY: tsa
tsa: $(JAR_TSA) $(JAR_AKKA) $(JAR_SCALA) $(JAR_CONFIG)
	@echo "  "
	@echo " ==================== BUILDING TSA Airport Screening ==================== "
	@$(MVN)
	@$(JAVA_FLAG) $(JAR_TSA):$(JAR_AKKA):$(JAR_SCALA):$(JAR_CONFIG) $(PROG)
	@echo "  "
	@echo " ==================== PROGRAM TSA Airport Screening MADE ================= "
	@echo "  "

.PHONY: file
file: $(tsa)
	@echo "  "
	@echo " ==================== BUILDING TSA Airport Screening ==================== "
	@$(MVN)
	@$(JAVA_FLAG) $(JAR_TSA):$(JAR_AKKA):$(JAR_SCALA):$(JAR_CONFIG) $(PROG) > $(TSA_FILE)
	@echo "  "
	@echo " ==================== PROGRAM TSA Airport Screening MADE ================= "
	@echo "  "

.PHONY: help
help:
	@echo "  "
	@echo "		" $(HELP_LIST)
	@echo "  "

.PHONY: clean
clean: 
	@echo "  "
	@echo "Removing $(TSA_FILE) file..."
	
	@rm -rf $(TSA_FILE)
	@echo "$(TSA_FILE) file removed."
	@echo "  "