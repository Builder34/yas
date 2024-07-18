import type { NextPage } from 'next';
import { Webhook } from '@webhookModels/Webhook';
import { createWebhook } from '@webhookServices/WebhookService';
import React from 'react';
import { useForm } from 'react-hook-form';
import Link from 'next/link';
import { useRouter } from 'next/router';
import WebhookInformation from '@webhookComponents/WebhookInformation';
import { SYSTEM_WEBHOOKS_URL } from 'constants/Common';
import { handleCreatingResponse } from '@commonServices/ResponseStatusHandlingService';

const WebhookCreate: NextPage = () => {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    trigger,
  } = useForm<Webhook>();
  const handleSubmitWebhook = async (event: Webhook) => {
    let Webhook: Webhook = {
      id: 0,
      payloadUrl: event.payloadUrl,
      secret: event.secret,
      status: event.status
    };
    let response = await createWebhook(Webhook);
    handleCreatingResponse(response);
    router.replace(SYSTEM_WEBHOOKS_URL);
  };

  return (
    <>
      <div className="row mt-5">
        <div className="col-md-8">
          <h2>Create Webhook</h2>
          <form onSubmit={handleSubmit(handleSubmitWebhook)}>
            <WebhookInformation
              register={register}
              errors={errors}
              setValue={setValue}
              trigger={trigger}
            />
            <button className="btn btn-primary" type="submit">
              Save
            </button>
            <Link href="/tax/tax-class">
              <button className="btn btn-primary" style={{ background: 'red', marginLeft: '30px' }}>
                Cancel
              </button>
            </Link>
          </form>
        </div>
      </div>
    </>
  );
};

export default WebhookCreate;
