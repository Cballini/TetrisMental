package com.project.matam.tetris.gamecore;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.emotiv.insight.FacialExpressionDetection;
import com.emotiv.insight.FacialExpressionDetection.IEE_FacialExpressionEvent_t;
import com.emotiv.insight.FacialExpressionDetection.IEE_FacialExpressionTrainingControl_t;
import com.emotiv.insight.IEdk;
import com.emotiv.insight.IEdkErrorCode;
import com.emotiv.insight.IEmoStateDLL;
import com.emotiv.insight.IEmoStateDLL.IEE_FacialExpressionAlgo_t;

import java.util.Timer;
import java.util.TimerTask;

public class EngineConnector {
	public static Context context;
	private static      EngineConnector instance;
	private Timer timer;
	private TimerTask timerTask;
	public boolean 		isConnected = false;
	private int 		state;
	int userId= 0; 
	
	protected static final int HANDLER_TRAINED_START    = 1;
	protected static final int HANDLER_TRAINED_SUCCEED  = 2;
	protected static final int HANDLER_TRAINED_COMPLETE = 3;
	protected static final int HANDLER_DETECT_REJECT   = 4;
	protected static final int HANDLER_DETECT_LOWER_FACE= 5;
	protected static final int HANDLER_TRAINED_ERASED   = 6;
	protected static final int HANDLER_USER_ADDED 		= 7;
	protected static final int HANDLER_USER_REMOVED 	= 8;
	protected static final int HANDLER_TRAIN_RESET 		= 9;
	///////
	protected static final int TYPE_USER_ADD      	  	= 16;
	protected static final int TYPE_USER_REMOVE     	= 32;
	protected static final int TYPE_EMOSTATE_UPDATE 	= 64;
	protected static final int TYPE_FACIAL_EVENT    	= 512;
	public EngineInterface	delegate;
	
	
	public static void setContext(Context context){
		EngineConnector.context = context;
	}
	
    public static EngineConnector shareInstance(){
		if (instance == null){
			instance = new EngineConnector();
		}
		return instance;
	}
  
    public EngineConnector(){
       connectEngine();
    }

   private void connectEngine(){
	   IEdk.IEE_EngineConnect(EngineConnector.context,"");
	   timer = new Timer();
	   intTimerTask();
	   timer.schedule(timerTask , 10, 10);
   }
   
   public void saveProfile(){

   }
   
   public void loadProfile(){
	   
   }
   public boolean startFacialExpression(Boolean isTrain, IEE_FacialExpressionAlgo_t FacialExpressionAction) {
		if (!isTrain) {
			if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingAction(userId,FacialExpressionAction.ToInt()) == IEdkErrorCode.EDK_OK.ToInt()) {
				if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId, IEE_FacialExpressionTrainingControl_t.FE_START.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
					return true;
				}
			}
		} else {
			if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId, IEE_FacialExpressionTrainingControl_t.FE_RESET.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
				return false;
			}
		}
		return false;
	}
   public void trainningClear(int _FacialdAction) {
	   FacialExpressionDetection.IEE_FacialExpressionSetTrainingAction(userId, _FacialdAction);
		if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId, IEE_FacialExpressionTrainingControl_t.FE_ERASE.getType()) == IEdkErrorCode.EDK_OK.ToInt()) {
		}
	}
   public void setTrainControl(int type) {
		if (FacialExpressionDetection.IEE_FacialExpressionSetTrainingControl(userId, type) == IEdkErrorCode.EDK_OK.ToInt()) {
		}
	}
   public boolean checkTrained(int action){
		long[] result = FacialExpressionDetection.IEE_FacialExpressionGetTrainedSignatureActions(userId);
		if (result[0] == 0) {
			long _currentTrainedActions = result[1];
		    long y = _currentTrainedActions & action;
		    return (y == action);
		}
		return false;
	}
   private void intTimerTask(){
	   if (timerTask != null) return;
	   timerTask = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub`
			/*Connect device with Insight headset*/
			 int numberDevice = IEdk.IEE_GetInsightDeviceCount();
				
			  if (numberDevice != 0){
			  if (!isConnected) 
			           IEdk.IEE_ConnectInsightDevice(0);
			  }
			/*************************************/
			/*Connect device with Epoc Plus headset*/
//			int numberDevice = IEdk.IEE_GetEpocPlusDeviceCount();
//
//			if (numberDevice != 0){
//				if (!isConnected)
//					IEdk.IEE_ConnectEpocPlusDevice(0,false);
//			}
			/*************************************/
			  	state = IEdk.IEE_EngineGetNextEvent();
			    if (state == IEdkErrorCode.EDK_OK.ToInt()) {
			    	   int eventType = IEdk.IEE_EmoEngineEventGetType();
			    	   switch (eventType) {
			    	   		case TYPE_USER_ADD:
			    	   			Log.e("FacialExpression", "User Added");
			    	   			isConnected = true;
			    	   			userId= IEdk.IEE_EmoEngineEventGetUserId();
			    	   			mHandler.sendMessage(mHandler.obtainMessage(HANDLER_USER_ADDED));
			    	   		     break;
			    	   		case TYPE_USER_REMOVE:
			    	   			Log.e("FacialExpression", "User Removed");
			    	   			isConnected = false;
			    	   			userId=-1;
			    	   	        mHandler.sendMessage(mHandler.obtainMessage(HANDLER_USER_REMOVED));
			    	   			break;
			    	   		case TYPE_EMOSTATE_UPDATE:
			    	   			if (!isConnected) break;
			    	   			IEdk.IEE_EmoEngineEventGetEmoState();
			    	   	        
			    	   			if (IEmoStateDLL.IS_FacialExpressionIsBlink() == 1) {
			    	   				Log.e("FacialExpression", "Blink");
			    	   			}
			    	   			if (IEmoStateDLL.IS_FacialExpressionIsEyesOpen() == 1){
			    	   			}
			    	   			mHandler.sendMessage(mHandler.obtainMessage(HANDLER_DETECT_LOWER_FACE));
			    	   		 break;
//			    	   		
			    	   		case TYPE_FACIAL_EVENT:
			    	   			int type = FacialExpressionDetection.IEE_FacialExpressionEventGetType();
			    	   		
			    	   			if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingCompleted.getType()){
			    	   				Log.e("FacialExpression", "training completed");
			    	   				mHandler.sendMessage(mHandler.obtainMessage(HANDLER_TRAINED_COMPLETE));
			    	   			}
			    	   			else 
			    	   			if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingDataErased.getType()){
				    	   				Log.e("FacialExpression", "training erased");
				    	   				mHandler.sendMessage(mHandler.obtainMessage(HANDLER_TRAINED_ERASED));
				    	   		}
			    	   			else 
			    	   			if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingFailed.getType()){
				    	   				Log.e("FacialExpression", "training failed");
				    	   		}
			    	   			else 
			    	   			if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingRejected.getType()){
				    	   				Log.e("FacialExpression", "training rejected");
				    	   			 	mHandler.sendMessage(mHandler.obtainMessage(HANDLER_DETECT_REJECT));
				    	   		}
			    	   			else 
				    	   		if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingReset.getType()){
					    	   		Log.e("FacialExpression", "training reseted");
					    	   		mHandler.sendMessage(mHandler.obtainMessage(HANDLER_TRAIN_RESET));
					    	   	}
				    	   		else 
					    	   	if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingStarted.getType()){
						    	   	Log.e("FacialExpression", "training started");
						    	   	mHandler.sendMessage(mHandler.obtainMessage(HANDLER_TRAINED_START));
						      	}
					    	   	else 
					    	   	if (type == IEE_FacialExpressionEvent_t.IEE_FacialExpressionTrainingSucceeded.getType()){
						    	   	Log.e("FacialExpression", "training succeeded");
						    	   	mHandler.sendMessage(mHandler.obtainMessage(HANDLER_TRAINED_SUCCEED));
						    	   	
						       	}
			    	   			break;
			    	   		default:
			    	   	         break;
			    	   }

			    }

		}
	};
   
   }
   
   // handler send to delegate
   public Handler mHandler = new Handler() {
       public void handleMessage(Message msg) {
       switch (msg.what) {
			case HANDLER_TRAINED_START:
				if (delegate!= null) {
	   	        	delegate.trainStarted(); 
				}
				break;
			case HANDLER_TRAINED_SUCCEED:
				if (delegate!= null) {
	   	        	delegate.trainSucceed(); 
				}
				break;
			case HANDLER_TRAINED_ERASED:
				if (delegate!= null) {
	   	        	delegate.trainErased(); 
				}
				break;
		   case HANDLER_DETECT_REJECT:
			   if(delegate != null)
			   {
				   delegate.trainRejected();
			   }
			   break;
			case HANDLER_TRAINED_COMPLETE:
				if (delegate!= null) {
	   	        	delegate.trainCompleted();
				}
				break;
			case HANDLER_TRAIN_RESET :
				if (delegate!= null) {
	   	        	delegate.trainReset();
				}
				break;
			case HANDLER_DETECT_LOWER_FACE:
				if (delegate!= null) {
					if(IEmoStateDLL.IS_FacialExpressionGetLowerFaceAction() ==128){
						delegate.detectedActionLowerFace("smile");
					}
					else if (IEmoStateDLL.IS_FacialExpressionIsLeftWink() == 1){
						delegate.detectedActionLowerFace("blinkLeft");
					}
					else if(IEmoStateDLL.IS_FacialExpressionIsRightWink() == 1){
						delegate.detectedActionLowerFace("blinkRight");
					}

					System.out.println("TEST expression ="+IEmoStateDLL.IS_FacialExpressionGetLowerFaceAction());
					System.out.println("TEST blink left ="+IEmoStateDLL.IS_FacialExpressionIsLeftWink());
					System.out.println("TEST blink right ="+IEmoStateDLL.IS_FacialExpressionIsRightWink());
				}
				break;	
			case HANDLER_USER_ADDED:
				if (delegate!= null) {
	   	        	delegate.userAdded(userId);
				}
				break;
			case HANDLER_USER_REMOVED:
				if (delegate!= null) {
	   	        	delegate.userRemove();
				}
				break;	
		    default:
			  break;
		  } 
       }
   } ;
}
