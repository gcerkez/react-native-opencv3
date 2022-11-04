// Create a header file and name it ‘ObjC_to_RN.h’
// #import <React/RCTBridgeModule.h>
// @interface SampleClass : NSObject <RCTBridgeModule>
// @end


#import <React/RCTEventEmitter.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <UIKit/UIKit.h>
#import "External.h"
#include <opencv2/img_hash/phash.hpp>
#include <opencv2/img_hash.hpp>

@interface RNOpencv3 : RCTEventEmitter <RCTBridgeModule>

@end
