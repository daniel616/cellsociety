package CellSociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Simulations.Simulation;
import UI_FrontEndGrid.BaseFrontEndGrid;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * class instantiates a single instance of a simulation, allowing simulations to
 * swap quickly
 * 
 * @author Moses Wayne
 *
 */
public class CellSocietyView {
	public static final double DEFAULT_SPEED = 150;

	private Simulation mySimulation;
	private BaseFrontEndGrid myDisplay;
	private Timeline cellAnimation;
	private List<ArrayList<Integer>> simHistory;

	private Integer simMaxVal;

	public CellSocietyView(Simulation newSimulation, BaseFrontEndGrid mainDisplay) {
		mySimulation = newSimulation;
		myDisplay = mainDisplay;
		simHistory = new ArrayList<ArrayList<Integer>>();
		simMaxVal = 0;
		initializeAnimation();
	}

	/**
	 * starts up the animation of the grid
	 */
	private void initializeAnimation() {
		KeyFrame gridFrame = new KeyFrame(Duration.millis(DEFAULT_SPEED), e -> getNextFrame());
		cellAnimation = new Timeline();
		cellAnimation.setCycleCount(Timeline.INDEFINITE);
		cellAnimation.getKeyFrames().add(gridFrame);
		updateSimHistory(mySimulation.getSimState());
	}

	public List<ArrayList<Integer>> getSimHistory() {
		return simHistory;
	}

	public void updateParams(Map<String, Double> newParams) {
		mySimulation.updateParameters(newParams);
	}

	/**
	 * plays the animation
	 */
	public void startOp() {
		cellAnimation.play();
	}

	/**
	 * stops the animation
	 */
	public void stopOp() {
		cellAnimation.pause();
	}

	/** 
	 * step function for the animation
	 */
	public void getNextFrame() {
		mySimulation.update();
		myDisplay.updateGrid();
		updateSimHistory(mySimulation.getSimState());
	}
	
	/**
	 * updates the simlation's history log
	 * @param simStates gets sim states from the simulations
	 */
	public void updateSimHistory(List<Integer> simStates) {
		if (simHistory.size() == 0) {
			for (int i = 0; i < simStates.size(); i++) {
				simHistory.add(new ArrayList<Integer>());
			}
		}
		for (int i = 0; i < simStates.size(); i++) {
			if (simStates.get(i) > simMaxVal) {
				simMaxVal = simStates.get(i);
			}
			simHistory.get(i).add(simStates.get(i));
		}
	}

	/**
	 * sets the animation speed
	 * @param newRate new animation speed
	 */
	public void setRate(double newRate) {
		cellAnimation.setRate(newRate);
	}

	/**
	 * returns the maximum value to graph
	 * @return max value
	 */
	public Integer getMaxSimVal() {
		return simMaxVal;
	}
}
