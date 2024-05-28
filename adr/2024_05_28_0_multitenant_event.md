## Overview
There are domain events describing a change of a state in the system.
For instance if someone were to create an estimate there could be an event fired.

To deliver and handle domain events we use messaging broker rabbitmq and spring boot cloud streams to handle that.

There are event publishers and event listeners.

## Problem
We have a multitenant system. Every action has to be executed on behalf of a certain workshop. How then event listeners can be aware of on behalf of which workshop should they execute their actions?

## Solution
Whenever a domain event is published from within domain layer there will be `workshopId` property added to it before publishing it. This is called `MultitenantDomainEvent`.
On the consumer side, when the event is dispatched, by given cloud stream function (e.g. CloudStreamConsumer),
executing thread gets authenticated on behalf of workshop specified by the property workshopId of MultitenantDomainEvent

## Example
- given event: StartedRepair(repairId: 1)
- given currently authenticated user of workshopId: some employee of workshopId = 2
- event json: { "repairId": 1 }
- message json published to message broker: { "workshopId": 2, "event": { "repairId": 1 } }
