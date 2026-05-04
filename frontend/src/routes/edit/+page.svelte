<script lang="ts">
  import { goto } from "$app/navigation";
  import { page } from "$app/state";
  import { currencyToId, idToCurrency, type CurrencyCode, type Currency } from "$lib/currency";

    let { params } = $props();

    const id = $derived(page.url.searchParams.get('id'));

    const account: Account = $derived(await fetch(`http://localhost:8084/api/v1/atm/${id}`).then(_ => _.json()));
    const accountFormDataInitialValue = $derived({
        ownerName: account.ownerName,
        balance: account.balance,
        accountStatus: account.accountStatus,
        currency: currencyToId[account.currency] });

    const accountFormData = $state((() => accountFormDataInitialValue)());

    async function confirmChanges() {
        await fetch(`http://localhost:8084/api/v1/atm/${id}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(accountFormData) });

        await goto('#/');
    }
</script>

<div class="flex w-screen h-screen justify-center items-center">
    <form method="POST" action="/api/v1/atm" class="flex flex-col gap-2 w-fit flex-wrap">
        <input bind:value={accountFormData.ownerName} placeholder="ownerName" class="input w-xs" />

        <select bind:value={accountFormData.accountStatus} class="select select-neutral uppercase">
            {#each ['active', 'frozen', 'closed'] as status}
                <option value={status} class="uppercase">{status}</option>
            {/each}
        </select>

        <div class="flex flex-row gap-2">
            <input bind:value={accountFormData.balance} placeholder="balance" type="number" class="input w-3/4" />

            <select bind:value={accountFormData.currency} class="select select-neutral uppercase w-1/4">
                {#each Object.entries(idToCurrency) as [ id, currency ]}
                    <option value={id} class="uppercase">{currency}</option>
                {/each}
            </select>
        </div>

        <button type="button" onclick={ confirmChanges } class="btn btn-outline btn-success">Подтвердить изменения</button>
    </form>
</div>