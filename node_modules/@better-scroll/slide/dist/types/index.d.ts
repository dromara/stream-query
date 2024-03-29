import BScroll from '@better-scroll/core';
import { EaseItem } from '@better-scroll/shared-utils';
import SlidePages, { Page } from './SlidePages';
export interface SlideConfig {
    loop: boolean;
    threshold: number;
    speed: number;
    easing: {
        style: string;
        fn: (t: number) => number;
    };
    listenFlick: boolean;
    autoplay: boolean;
    interval: number;
    startPageXIndex: number;
    startPageYIndex: number;
}
export declare type SlideOptions = Partial<SlideConfig> | true;
declare module '@better-scroll/core' {
    interface CustomOptions {
        slide?: SlideOptions;
    }
    interface CustomAPI {
        slide: PluginAPI;
    }
}
interface PluginAPI {
    next(time?: number, easing?: EaseItem): void;
    prev(time?: number, easing?: EaseItem): void;
    goToPage(x: number, y: number, time?: number, easing?: EaseItem): void;
    getCurrentPage(): Page;
    startPlay(): void;
    pausePlay(): void;
}
export default class Slide implements PluginAPI {
    scroll: BScroll;
    static pluginName: string;
    pages: SlidePages;
    options: SlideConfig;
    initialised: boolean;
    contentChanged: boolean;
    prevContent: HTMLElement;
    exposedPage: Page;
    private cachedClonedPageDOM;
    private oneToMorePagesInLoop;
    private moreToOnePageInLoop;
    private thresholdX;
    private thresholdY;
    private hooksFn;
    private resetLooping;
    private willChangeToPage;
    private autoplayTimer;
    constructor(scroll: BScroll);
    private satisfyInitialization;
    init(): void;
    private createPages;
    private handleBScroll;
    private handleOptions;
    private handleLoop;
    private resetLoopChangedStatus;
    private handleHooks;
    startPlay(): void;
    pausePlay(): void;
    private setSlideInlineStyle;
    next(time?: number, easing?: EaseItem): void;
    prev(time?: number, easing?: EaseItem): void;
    goToPage(pageX: number, pageY: number, time?: number, easing?: EaseItem): void;
    getCurrentPage(): Page;
    setCurrentPage(page: Page): void;
    nearestPage(x: number, y: number): Page;
    private satisfyThreshold;
    private refreshHandler;
    private computeThreshold;
    private cloneFirstAndLastSlidePage;
    private removeClonedSlidePage;
    private modifyCurrentPage;
    private goTo;
    private flickHandler;
    private getEaseTime;
    private modifyScrollMetaHandler;
    private scrollHandler;
    private pageWillChangeTo;
    private registerHooks;
    destroy(): void;
}
export {};
