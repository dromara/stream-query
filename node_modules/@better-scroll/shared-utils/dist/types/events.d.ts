interface Events {
    [name: string]: [WithFnFunction, Object][];
}
interface EventTypes {
    [type: string]: string;
}
interface WithFnFunction extends Function {
    fn?: Function;
}
export declare class EventEmitter {
    events: Events;
    eventTypes: EventTypes;
    constructor(names: string[]);
    on(type: string, fn: Function, context?: Object): this;
    once(type: string, fn: Function, context?: Object): this;
    off(type?: string, fn?: Function): this | undefined;
    trigger(type: string, ...args: any[]): any;
    registerType(names: string[]): void;
    destroy(): void;
    private hasType;
}
interface EventData {
    name: string;
    handler(e: UIEvent): void;
    capture?: boolean;
}
export declare class EventRegister {
    wrapper: HTMLElement | Window;
    events: EventData[];
    constructor(wrapper: HTMLElement | Window, events: EventData[]);
    destroy(): void;
    private addDOMEvents;
    private removeDOMEvents;
    private handleDOMEvents;
    private handleEvent;
}
export {};
