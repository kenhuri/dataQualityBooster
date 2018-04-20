import { BaseEntity } from './../../shared';

export class PythonDataBooster implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public server?: string,
        public login?: string,
        public keySSH?: any,
        public defaultParameter?: string,
    ) {
    }
}
