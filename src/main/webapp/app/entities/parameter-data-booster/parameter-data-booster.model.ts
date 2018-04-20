import { BaseEntity } from './../../shared';

export class ParameterDataBooster implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public value?: any,
        public contextId?: number,
    ) {
    }
}
