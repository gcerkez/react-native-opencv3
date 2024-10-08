// @author Adam G. Freeman - adamgf@gmail.com

#import "RNOpencv3.h"

@implementation RNOpencv3

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

- (NSString *)getHash:(NSString*)filePath {
    UIImage *sourceImage = [UIImage imageWithContentsOfFile:filePath];
    Mat srcMat;
    UIImageToMat(sourceImage, srcMat);
    Mat outMat;
    cv::img_hash::pHash(srcMat, outMat);
    std::ostringstream s;
        
    for (int i = 0; i<outMat.cols; i++)
        s << std::hex<<int(outMat.at<uchar>(0, i));
        
    NSString *hash = [NSString stringWithCString:s.str().c_str() encoding:[NSString defaultCStringEncoding]];
    return hash;
}

RCT_EXPORT_METHOD(jsPhash:(NSString*)filePath resolver:(RCTPromiseResolveBlock)resolve
     rejecter:(RCTPromiseRejectBlock)reject) {
    NSLog(@"Value of filepath in jsPhash = %@", filePath);
    NSString *hash = [self getHash:filePath];
    NSLog(@"Value of hash = %@", hash);
    if (hash) {
        resolve(hash);
    } 
};


- (NSArray<NSString *> *)supportedEvents
{
    return @[@"onPayload"];
}

@end
