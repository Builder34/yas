import { NextPage } from 'next';
import Link from 'next/link';
import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';

import { handleUpdatingResponse } from '@commonServices/ResponseStatusHandlingService';
import { toastError } from '@commonServices/ToastService';
import WebhookGeneralInformation from '@webhookComponents/WebhookInformation';
import { Webhook } from '@webhookModels/Webhook';
import { updateWebhook, getWebhook } from '@webhookServices/WebhookService';
import { SYSTEM_WEBHOOKS_URL } from 'constants/Common';

const WebhookEdit: NextPage = () => {
  const router = useRouter();
  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    trigger,
  } = useForm<Webhook>();
  const [webhook, setWebhook] = useState<Webhook>();
  const [isLoading, setLoading] = useState(false);
  const { id } = router.query;
  const handleSubmitEdit = async (event: Webhook) => {
    if (id) {
      let webhook: Webhook = {
        id: 0,
        payloadUrl: event.payloadUrl,
        secret: event.secret,
        status: event.status
      };

      updateWebhook(+id, webhook)
        .then((response) => {
          handleUpdatingResponse(response);
          router.replace(SYSTEM_WEBHOOKS_URL).catch((error) => console.log(error));
        })
        .catch((error) => console.log(error));
    }
  };

  useEffect(() => {
    if (id) {
      setLoading(true);
      getWebhook(+id)
        .then((data) => {
          if (data.id) {
            setWebhook(data);
            setLoading(false);
          } else {
            toastError(data?.payloadUrl);
            setLoading(false);
            router.push(SYSTEM_WEBHOOKS_URL).catch((error) => console.log(error));
          }
        })
        .catch((error) => console.log(error));
    }
  }, [id]);

  if (isLoading) return <p>Loading...</p>;
  if (!webhook) return <></>;
  return (
    <>
      <div className="row mt-5">
        <div className="col-md-8">
          <h2>Edit webhook Class: {id}</h2>
          <form onSubmit={handleSubmit(handleSubmitEdit)}>
            <WebhookGeneralInformation
              register={register}
              errors={errors}
              setValue={setValue}
              trigger={trigger}
              webhook={webhook}
            />

            <button className="btn btn-primary" type="submit">
              Save
            </button>
            <Link href="webhook/webhook-classes">
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

export default WebhookEdit;
