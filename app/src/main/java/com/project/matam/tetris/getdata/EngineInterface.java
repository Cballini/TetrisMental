package com.project.matam.tetris.getdata;

public interface EngineInterface {
	//train
	public void trainStarted();
	public void trainSucceed();
	public void trainFailed();
	public void trainCompleted();
	public void trainRejected();
	public void trainReset();
	public void trainErased();
	public void userAdd(int userId);
	public void userRemoved();
	
	//action
	public void currentAction(int typeAction,float power);
	
	
}
