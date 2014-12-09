package coinffeine.model.exchange

import coinffeine.model.bitcoin.ImmutableTransaction
import coinffeine.model.currency.FiatCurrency

case class NotStartedExchange[C <: FiatCurrency](metadata: ExchangeMetadata[C]) extends Exchange[C] {

  override val status = "not started"
  override val progress = Exchange.noProgress(currency)
  override val isCompleted = false

  def startHandshaking(user: Exchange.PeerInfo,
                       counterpart: Exchange.PeerInfo): HandshakingExchange[C] =
    HandshakingExchange(this, user, counterpart)

  def cancel(cause: CancellationCause,
             user: Option[Exchange.PeerInfo] = None): FailedExchange[C] =
    FailedExchange(this, FailureCause.Cancellation(cause), user)

  def abort(cause: AbortionCause,
            user: Exchange.PeerInfo,
            refundTx: ImmutableTransaction): AbortingExchange[C] =
    AbortingExchange(this, cause, user, refundTx)

  def withId(id: ExchangeId) = copy(metadata = metadata.copy(id = id))
}
