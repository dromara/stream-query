import Base from './Base';
import { TranslaterPoint } from '../translater';
import { EaseFn } from '@better-scroll/shared-utils';
export default class Animation extends Base {
    move(startPoint: TranslaterPoint, endPoint: TranslaterPoint, time: number, easingFn: EaseFn | string): void;
    private animate;
    doStop(): boolean;
    stop(): void;
}
