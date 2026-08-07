import { EnvironmentProviders, makeEnvironmentProviders } from '@angular/core';

export function provideCore(): EnvironmentProviders {
    return makeEnvironmentProviders([
        // Core providers will be added here
    ]);
}