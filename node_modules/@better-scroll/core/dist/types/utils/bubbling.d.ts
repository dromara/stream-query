import { EventEmitter } from '@better-scroll/shared-utils';
interface BubblingEventMap {
    source: string;
    target: string;
}
declare type BubblingEventConfig = BubblingEventMap | string;
export declare function bubbling(source: EventEmitter, target: EventEmitter, events: BubblingEventConfig[]): void;
export {};
