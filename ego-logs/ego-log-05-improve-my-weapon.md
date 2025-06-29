I realize my weapon isn't power but something detached from the field, the ability to see the minute picture that affects the big picture in a massive way.
Honing my weapon until it shimmers is my new ego.

Listened to podcast https://www.youtube.com/watch?v=3E_jDJST69s&t=2973s
Absolutely GOATED mindset and work ethic.

I'll be reborn.

The think I just realized now is that hibernate already has the multi tenancy data isolation part figured out can I use to my advantage and adapt and score my goal?
I can restructure the entire TenantDataSource to have hibernate driven support but also it must be opinionated and default but consumer app can make their own.

Here's the plan:
1. Integrate the hibernate multi tenancy data isolation with my   multi-tenancy.isolation.type: TENANT_PER_DATABASE | TENANT_PER_SCHEMA | SHARED_DB which will correspond to the hibernate properties.
2. I will need to change my filter according to interfaces provided by hibernate.
3. I will need to create core annotations which enables this and multitenancy as a whole features.


after prompt:

Would you like me to help you structure this into a tenant bootstrap lifecycle (registry → validation → schema creation → datasource bind)?