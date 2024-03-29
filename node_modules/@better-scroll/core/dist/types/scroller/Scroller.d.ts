import ActionsHandler from '../base/ActionsHandler';
import Translater, { TranslaterPoint } from '../translater';
import { Animater } from '../animater';
import { OptionsConstructor as BScrollOptions } from '../Options';
import { Behavior } from './Behavior';
import ScrollerActions from './Actions';
import { EaseItem, EventEmitter, EventRegister } from '@better-scroll/shared-utils';
export interface ExposedAPI {
    scrollTo(x: number, y: number, time?: number, easing?: EaseItem, extraTransform?: {
        start: object;
        end: object;
    }): void;
    scrollBy(deltaX: number, deltaY: number, time?: number, easing?: EaseItem): void;
    scrollToElement(el: HTMLElement | string, time: number, offsetX: number | boolean, offsetY: number | boolean, easing?: EaseItem): void;
    resetPosition(time?: number, easing?: EaseItem): boolean;
}
export default class Scroller implements ExposedAPI {
    wrapper: HTMLElement;
    content: HTMLElement;
    actionsHandler: ActionsHandler;
    translater: Translater;
    animater: Animater;
    scrollBehaviorX: Behavior;
    scrollBehaviorY: Behavior;
    actions: ScrollerActions;
    hooks: EventEmitter;
    resizeRegister: EventRegister;
    transitionEndRegister: EventRegister;
    options: BScrollOptions;
    wrapperOffset: {
        left: number;
        top: number;
    };
    _reflow: number;
    resizeTimeout: number;
    lastClickTime: number | null;
    [key: string]: any;
    constructor(wrapper: HTMLElement, content: HTMLElement, options: BScrollOptions);
    private init;
    private registerTransitionEnd;
    private bindTranslater;
    private bindAnimater;
    private bindActions;
    private checkFlick;
    private momentum;
    private checkClick;
    private resize;
    private transitionEnd;
    togglePointerEvents(enabled?: boolean): void;
    refresh(content: HTMLElement): void;
    private setContent;
    scrollBy(deltaX: number, deltaY: number, time?: number, easing?: EaseItem): void;
    scrollTo(x: number, y: number, time?: number, easing?: EaseItem, extraTransform?: {
        start: {};
        end: {};
    }): void;
    scrollToElement(el: HTMLElement | string, time: number, offsetX: number | boolean, offsetY: number | boolean, easing?: EaseItem): void;
    resetPosition(time?: number, easing?: EaseItem): boolean;
    reflow(): void;
    updatePositions(pos: TranslaterPoint): void;
    getCurrentPos(): TranslaterPoint;
    enable(): void;
    disable(): void;
    destroy(this: Scroller): void;
}
