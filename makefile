JAVA_SOURCES = $(shell find src -name *.java)
OUT_FOLDER = out

compile:$(JAVA_SOURCES)
	mkdir -p $(OUT_FOLDER)
	javac -d $(OUT_FOLDER) $(JAVA_SOURCES)

run:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.SimulationApp

experimentVaryingTimeStepExperiment:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.VaryingTimeStepExperiment
	
experimentEnergyExperiment:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.EnergyExperiment

experimentSideEscapingVaryingSpeedExperiment:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.SideEscapingVaryingSpeedExperiment

experimentTrajectoryVaryingSpeedExperiment:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.TrajectoryVaryingSpeedExperiment

# You should run simulation with the corresponding system
# change the corresponding system and make sure the output
# filename is the correct one
graphs_ej1:
	cd visualization
	bash -c "source .env/bin/activate; python graphs_ej1.py"
graphs_ej2_1:
	cd visualization
	bash -c "source .env/bin/activate; python graphs_ej2_1.py"

# You should run the correct experiment before any of this commands
graphs_ej1_2:
	cd visualization
	bash -c "source .env/bin/activate; python graphs_ej1_2.py"
graphs_ej2_2:
	cd visualization
	bash -c "source .env/bin/activate; python graphs_ej2_2.py"
graphs_ej2_3:
	cd visualization
	bash -c "source .env/bin/activate; python graphs_ej2_2.py"
graphs_ej2_4:
	cd visualization
	bash -c "source .env/bin/activate; python graphs_ej2_2.py"