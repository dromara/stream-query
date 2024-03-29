import { Quadrant } from '@better-scroll/shared-utils';
export declare type Tap = 'tap' | '';
export declare type BounceOptions = Partial<BounceConfig> | boolean;
export declare type DblclickOptions = Partial<DblclickConfig> | boolean;
export interface BounceConfig {
    top: boolean;
    bottom: boolean;
    left: boolean;
    right: boolean;
}
export interface DblclickConfig {
    delay: number;
}
export interface CustomOptions {
}
export interface DefOptions {
    [key: string]: any;
    startX?: number;
    startY?: number;
    scrollX?: boolean;
    scrollY?: boolean;
    freeScroll?: boolean;
    directionLockThreshold?: number;
    eventPassthrough?: string;
    click?: boolean;
    tap?: Tap;
    bounce?: BounceOptions;
    bounceTime?: number;
    momentum?: boolean;
    momentumLimitTime?: number;
    momentumLimitDistance?: number;
    swipeTime?: number;
    swipeBounceTime?: number;
    deceleration?: number;
    flickLimitTime?: number;
    flickLimitDistance?: number;
    resizePolling?: number;
    probeType?: number;
    stopPropagation?: boolean;
    preventDefault?: boolean;
    preventDefaultException?: {
        tagName?: RegExp;
        className?: RegExp;
    };
    tagException?: {
        tagName?: RegExp;
        className?: RegExp;
    };
    HWCompositing?: boolean;
    useTransition?: boolean;
    bindToWrapper?: boolean;
    bindToTarget?: boolean;
    disableMouse?: boolean;
    disableTouch?: boolean;
    autoBlur?: boolean;
    translateZ?: string;
    dblclick?: DblclickOptions;
    autoEndDistance?: number;
    outOfBoundaryDampingFactor?: number;
    specifiedIndexAsContent?: number;
    quadrant?: Quadrant;
}
export interface Options extends DefOptions, CustomOptions {
}
export declare class CustomOptions {
}
export declare class OptionsConstructor extends CustomOptions implements DefOptions {
    [key: string]: any;
    startX: number;
    startY: number;
    scrollX: boolean;
    scrollY: boolean;
    freeScroll: boolean;
    directionLockThreshold: number;
    eventPassthrough: string;
    click: boolean;
    tap: Tap;
    bounce: BounceConfig;
    bounceTime: number;
    momentum: boolean;
    momentumLimitTime: number;
    momentumLimitDistance: number;
    swipeTime: number;
    swipeBounceTime: number;
    deceleration: number;
    flickLimitTime: number;
    flickLimitDistance: number;
    resizePolling: number;
    probeType: number;
    stopPropagation: boolean;
    preventDefault: boolean;
    preventDefaultException: {
        tagName?: RegExp;
        className?: RegExp;
    };
    tagException: {
        tagName?: RegExp;
        className?: RegExp;
    };
    HWCompositing: boolean;
    useTransition: boolean;
    bindToWrapper: boolean;
    bindToTarget: boolean;
    disableMouse: boolean;
    disableTouch: boolean;
    autoBlur: boolean;
    translateZ: string;
    dblclick: DblclickOptions;
    autoEndDistance: number;
    outOfBoundaryDampingFactor: number;
    specifiedIndexAsContent: number;
    quadrant: Quadrant;
    constructor();
    merge(options?: Options): this;
    process(): this;
    resolveBounce(bounceOptions: BounceOptions): BounceConfig;
}
