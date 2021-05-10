JAVA_SOURCES = $(shell find simulation -name *.java)
OUT_FOLDER = out

compile:$(JAVA_SOURCES)
	mkdir -p $(OUT_FOLDER)
	javac -d $(OUT_FOLDER) $(JAVA_SOURCES)

run_electric:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.simulation.SimulationElectricApp

run_oscillatory:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.simulation.SimulationOscillatoryApp

experimentVaryingTimeStepExperiment:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.experiment.VaryingTimeStepExperiment
	
experimentEnergyExperiment:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.experiment.EnergyExperiment

experimentSideEscapingVaryingSpeedExperiment:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.experiment.SideEscapingVaryingSpeedExperiment

experimentTrajectoryVaryingSpeedExperiment:compile
	java -cp $(OUT_FOLDER) ar.edu.itba.experiment.TrajectoryVaryingSpeedExperiment

# You should run run_oscillatory before this
graphs_ej1:
	bash -c "cd visualization;source .env/bin/activate; python graphs_ej1.py"
# You should run run_electric before this
graphs_ej2_1:
	bash -c "cd visualization;source .env/bin/activate; python graphs_ej2_1.py"

# You should run the correct experiment before any of this commands
graphs_ej1_2:
	bash -c "cd visualization;source .env/bin/activate; python graphs_ej1_2.py"
graphs_ej2_2:
	bash -c "cd visualization;source .env/bin/activate; python graphs_ej2_2.py"
graphs_ej2_3:
	bash -c "cd visualization;source .env/bin/activate; python graphs_ej2_2.py"
graphs_ej2_4:
	bash -c "cd visualization;source .env/bin/activate; python graphs_ej2_2.py"