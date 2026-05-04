<script lang="ts">
  import { goto } from "$app/navigation";
  import { statusCodeToName } from "$lib/localization";

    let confirmAccountDeleteModal: HTMLDialogElement;
    let accountToDelete: Account = $state({} as any);
    
    let accounts: Account[] = $state([]);        
    const addAccountFormData = $state({
        ownerName: undefined, balance: undefined, currency: undefined });

    async function refreshAccounts() {
        accounts = await fetch('http://localhost:8084/api/v1/atm').then(_ => _.json());
    }
    
    async function addAccount() {
        await fetch('http://localhost:8084/api/v1/atm', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(addAccountFormData) });
        
        await refreshAccounts();
    }

    const promptDeleteAccount = (account: Account) => () => {
        accountToDelete = account;
        confirmAccountDeleteModal.showModal();
    }

    async function deleteAccount() {
        await fetch(`http://localhost:8084/api/v1/atm/${accountToDelete.id}`, { method: 'DELETE' });
        await refreshAccounts();
    }

    refreshAccounts();
</script>

<div class="flex w-screen justify-center">
    <div class="pt-20 flex flex-col gap-2 w-2xl pb-4">
        <div class="w-full flex justify-center" >
            <form method="POST" action="/api/v1/atm" class="flex flex-col gap-2 w-fit">
                <input bind:value={addAccountFormData.ownerName} placeholder="Имя" class="input w-xs" />
                <input bind:value={addAccountFormData.balance} placeholder="Баланс" type="number" class="input w-xs" />
                <input bind:value={addAccountFormData.currency} placeholder="Код валюты" type="number" class="input w-xs" />
                
                <button type="button" onclick={ addAccount } class="btn btn-outline btn-success">Создать</button>
            </form>
        </div>

        <ul class="list bg-base-100 rounded-box shadow-md">
            <li class="p-4 pb-2 text-xs opacity-60 tracking-wide">Аккаунты</li>
            
            {#each accounts as account}
                <li class="list-row">
                    <div>
                        <div class="flex flex-row items-center gap-1">
                            <div>{ account.ownerName }</div>
                            <p class="text-xs font-semibold opacity-60">({ statusCodeToName[account.accountStatus] })</p>
                        </div>
                        <div class="flex flex-row items-center gap-1">
                            <div>{ account.balance }</div>
                            <p class="text-xs font-semibold opacity-60 uppercase">{ account.currency }</p>
                        </div>
                    </div>
                    <div></div>
                    <button class="btn btn-square btn-ghost" title="Изменить" aria-label="edit" onclick={ () => goto(`?id=${account.id}#/edit`) }>
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#e3e3e3">
                            <path d="M200-200h57l391-391-57-57-391 391v57Zm-80 80v-170l528-527q12-11 26.5-17t30.5-6q16 0 31 6t26 18l55 56q12 11 17.5 26t5.5 30q0 16-5.5 30.5T817-647L290-120H120Zm640-584-56-56 56 56Zm-141 85-28-29 57 57-29-28Z"/>
                        </svg>
                    </button>
                    <button class="btn btn-square btn-ghost" title="Удалить" aria-label="delete" onclick={ promptDeleteAccount(account) }>
                        <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#e3e3e3">
                            <path d="M280-120q-33 0-56.5-23.5T200-200v-520h-40v-80h200v-40h240v40h200v80h-40v520q0 33-23.5 56.5T680-120H280Zm400-600H280v520h400v-520ZM360-280h80v-360h-80v360Zm160 0h80v-360h-80v360ZM280-720v520-520Z"/>
                        </svg>
                    </button>
                </li>
            {/each}
        </ul>
        
        <dialog bind:this={ confirmAccountDeleteModal } class="modal modal-bottom sm:modal-middle">
            <div class="modal-box">
                <h3 class="text-lg font-bold">Вы уверены?</h3>
                <p class="py-4">Вы удаляете аккаунт на имя "{ accountToDelete.ownerName }", валюта "{ accountToDelete.currency }"</p>
                <div class="modal-action">
                    <form method="dialog">
                        <button class="btn">Отмена</button>
                        <button class="btn" onclick={ deleteAccount }>Подтвердить</button>
                    </form>
                </div>
            </div>

            <form method="dialog" class="modal-backdrop">
                <button>close</button>
            </form>
        </dialog>

        {#if !accounts?.length}
            <p class="text-center">Аккаунтов пока нет.</p>
        {/if}
    </div>
</div>