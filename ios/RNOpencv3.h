// Create a header file and name it ‘ObjC_to_RN.h’
// #import <React/RCTBridgeModule.h>
// @interface SampleClass : NSObject <RCTBridgeModule>
// @end


#import <React/RCTEventEmitter.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <UIKit/UIKit.h>

@interface RNOpencv3 : RCTEventEmitter <RCTBridgeModule>

@end
