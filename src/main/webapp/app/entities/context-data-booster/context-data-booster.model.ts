import { BaseEntity } from './../../shared';

export class ContextDataBooster implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public client?: string,
        public imageContentType?: string,
        public image?: any,
        public pythonId?: number,
    ) {
    }
}
