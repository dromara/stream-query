import { EventRegister, EventEmitter } from '@better-scroll/shared-utils';
declare type Exception = {
    tagName?: RegExp;
    className?: RegExp;
};
export interface Options {
    [key: string]: boolean | number | Exception;
    click: boolean;
    bindToWrapper: boolean;
    disableMouse: boolean;
    disableTouch: boolean;
    preventDefault: boolean;
    stopPropagation: boolean;
    preventDefaultException: Exception;
    tagException: Exception;
    autoEndDistance: number;
}
export default class ActionsHandler {
    wrapper: HTMLElement;
    options: Options;
    hooks: EventEmitter;
    initiated: number;
    pointX: number;
    pointY: number;
    wrapperEventRegister: EventRegister;
    targetEventRegister: EventRegister;
    constructor(wrapper: HTMLElement, options: Options);
    private handleDOMEvents;
    private beforeHandler;
    setInitiated(type?: number): void;
    private start;
    private move;
    private end;
    private click;
    setContent(content: HTMLElement): void;
    rebindDOMEvents(): void;
    destroy(): void;
}
export {};
