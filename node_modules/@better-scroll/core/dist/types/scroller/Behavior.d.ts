import { EventEmitter } from '@better-scroll/shared-utils';
export declare type Bounces = [boolean, boolean];
export declare type Rect = {
    size: string;
    position: string;
};
export interface Options {
    scrollable: boolean;
    momentum: boolean;
    momentumLimitTime: number;
    momentumLimitDistance: number;
    deceleration: number;
    swipeBounceTime: number;
    swipeTime: number;
    bounces: Bounces;
    rect: Rect;
    outOfBoundaryDampingFactor: number;
    specifiedIndexAsContent: number;
    [key: string]: number | boolean | Bounces | Rect;
}
export declare type Boundary = {
    minScrollPos: number;
    maxScrollPos: number;
};
export declare class Behavior {
    wrapper: HTMLElement;
    options: Options;
    content: HTMLElement;
    currentPos: number;
    startPos: number;
    absStartPos: number;
    dist: number;
    minScrollPos: number;
    maxScrollPos: number;
    hasScroll: boolean;
    direction: number;
    movingDirection: number;
    relativeOffset: number;
    wrapperSize: number;
    contentSize: number;
    hooks: EventEmitter;
    constructor(wrapper: HTMLElement, content: HTMLElement, options: Options);
    start(): void;
    move(delta: number): number;
    setMovingDirection(delta: number): void;
    setDirection(delta: number): void;
    performDampingAlgorithm(delta: number, dampingFactor: number): number;
    end(duration: number): {
        destination?: number | undefined;
        duration?: number | undefined;
    };
    private momentum;
    updateDirection(): void;
    refresh(content: HTMLElement): void;
    private setContent;
    private resetState;
    computeBoundary(): void;
    updatePosition(pos: number): void;
    getCurrentPos(): number;
    checkInBoundary(): {
        position: number;
        inBoundary: boolean;
    };
    adjustPosition(pos: number): number;
    updateStartPos(): void;
    updateAbsStartPos(): void;
    resetStartPos(): void;
    getAbsDist(delta: number): number;
    destroy(): void;
}
