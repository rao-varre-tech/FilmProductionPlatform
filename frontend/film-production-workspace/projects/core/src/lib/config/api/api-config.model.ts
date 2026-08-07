export interface ApiConfig {
    gateway: string;
    timeout: number;

    endpoints: {
        auth: string;
        users: string;
        roles: string;
        permissions: string;
    };
}