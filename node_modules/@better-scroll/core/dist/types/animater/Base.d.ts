import { EaseFn, safeCSSStyleDeclaration, EventEmitter } from '@better-scroll/shared-utils';
import Translater, { TranslaterPoint } from '../translater';
export interface ExposedAPI {
    stop(): void;
}
export default abstract class Base implements ExposedAPI {
    translater: Translater;
    options: {
        probeType: number;
    };
    content: HTMLElement;
    style: safeCSSStyleDeclaration;
    hooks: EventEmitter;
    timer: number;
    pending: boolean;
    callStopWhenPending: boolean;
    forceStopped: boolean;
    _reflow: number;
    [key: string]: any;
    constructor(content: HTMLElement, translater: Translater, options: {
        probeType: number;
    });
    translate(endPoint: TranslaterPoint): void;
    setPending(pending: boolean): void;
    setForceStopped(forceStopped: boolean): void;
    setCallStop(called: boolean): void;
    setContent(content: HTMLElement): void;
    clearTimer(): void;
    abstract move(startPoint: TranslaterPoint, endPoint: TranslaterPoint, time: number, easing: string | EaseFn): void;
    abstract doStop(): void;
    abstract stop(): void;
    destroy(): void;
}
