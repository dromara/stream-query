import BScroll from '@better-scroll/core';
import { SlideConfig } from './index';
import PagesMatrix, { PageStats } from './PagesMatrix';
export interface PageIndex {
    pageX: number;
    pageY: number;
}
export interface Position {
    x: number;
    y: number;
}
export declare type Page = PageIndex & Position;
export default class SlidePages {
    scroll: BScroll;
    private slideOptions;
    loopX: boolean;
    loopY: boolean;
    slideX: boolean;
    slideY: boolean;
    wannaLoop: boolean;
    pagesMatrix: PagesMatrix;
    currentPage: Page;
    constructor(scroll: BScroll, slideOptions: SlideConfig);
    refresh(): void;
    getAdjustedCurrentPage(): Page;
    setCurrentPage(newPage: Page): void;
    getInternalPage(pageX: number, pageY: number): Page;
    getInitialPage(showFirstPage?: boolean, firstInitialised?: boolean): Page;
    getExposedPage(page: Page): Page;
    getExposedPageByPageIndex(pageIndexX: number, pageIndexY: number): Page;
    getWillChangedPage(page: Page): Page;
    private fixedPage;
    getPageStats(): PageStats;
    getValidPageIndex(x: number, y: number): PageIndex;
    nextPageIndex(): PageIndex;
    prevPageIndex(): PageIndex;
    getNearestPage(x: number, y: number): Page;
    getPageByDirection(page: Page, directionX: number, directionY: number): Page;
    resetLoopPage(): PageIndex | undefined;
    private getPageIndexByDirection;
    private checkSlideLoop;
}
