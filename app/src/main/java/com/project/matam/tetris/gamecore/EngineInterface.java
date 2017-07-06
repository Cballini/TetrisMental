package com.project.matam.tetris.gamecore;

public interface EngineInterface {

	//train
	public void trainStarted();
	public void trainSucceed();
	public void trainCompleted();
	public void trainRejected();
	public void trainErased();
	public void trainReset();
	public void userAdded(int userId);
	public void userRemove();


	// detection
	public void detectedActionLowerFace(String typeAction);
}
