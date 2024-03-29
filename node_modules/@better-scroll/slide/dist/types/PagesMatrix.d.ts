import BScroll from '@better-scroll/core';
import { PageIndex } from './SlidePages';
export interface PageStats {
    x: number;
    y: number;
    width: number;
    height: number;
    cx: number;
    cy: number;
}
export default class PagesMatrix {
    private scroll;
    pages: Array<Array<PageStats>>;
    pageLengthOfX: number;
    pageLengthOfY: number;
    private wrapperWidth;
    private wrapperHeight;
    private scrollerWidth;
    private scrollerHeight;
    constructor(scroll: BScroll);
    init(): void;
    getPageStats(pageX: number, pageY: number): PageStats;
    getNearestPageIndex(x: number, y: number): PageIndex;
    private buildPagesMatrix;
}
