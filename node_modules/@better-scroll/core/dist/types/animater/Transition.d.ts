import { EaseFn } from '@better-scroll/shared-utils';
import Base from './Base';
import { TranslaterPoint } from '../translater';
export default class Transition extends Base {
    startProbe(startPoint: TranslaterPoint, endPoint: TranslaterPoint): void;
    transitionTime(time?: number): void;
    transitionTimingFunction(easing: string): void;
    transitionProperty(): void;
    move(startPoint: TranslaterPoint, endPoint: TranslaterPoint, time: number, easingFn: string | EaseFn): void;
    doStop(): boolean;
    stop(): void;
}
